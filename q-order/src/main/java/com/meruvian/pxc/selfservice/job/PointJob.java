package com.meruvian.pxc.selfservice.job;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.entity.MainBody;
import com.meruvian.pxc.selfservice.entity.Point;
import com.meruvian.pxc.selfservice.service.JobStatus;
import com.meruvian.pxc.selfservice.service.PointService;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by akm on 04/02/16.
 */
public class PointJob extends Job {

    private SharedPreferences preferences;

    protected PointJob() {
        super(new Params(1).requireNetwork().persist());
    }

    public static PointJob newInstance() {

        PointJob pointJob = new PointJob();

        return pointJob;

    }
    @Override
    public void onAdded() {

        Log.d("STARTING", "JOB");
        EventBus.getDefault().post(JobStatus.ADDED);
    }

    @Override
    public void onRun() throws Throwable {
        Log.d("Job", "Started");
        SignageAppication appication = SignageAppication.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("access_token", AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        try {
            PointService service = appication.getRetrofit().create(PointService.class);
            final Call<MainBody<Point>> point = service.getUserPoint(param);
            point.enqueue(new Callback<MainBody<Point>>() {
                @Override
                public void onResponse(Response<MainBody<Point>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        MainBody<Point> mainBody = response.body();
                        if (response.code() == 204) {
                            EventBus.getDefault().post(new PointEvent(JobStatus.USER_ERROR, 0));
                        } else if (response.code() == 200) {
                            for (Point p : mainBody.getContent()) {
                                Log.d("POINT", String.valueOf(p.getPoint()));
                                EventBus.getDefault().post(new PointEvent(JobStatus.SUCCESS, p.getPoint()));
                            }
                        }

                    } else {
                        EventBus.getDefault().post(new PointEvent(JobStatus.USER_ERROR, 0));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    EventBus.getDefault().post(new PointEvent(JobStatus.USER_ERROR, 0));

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new PointEvent(JobStatus.ABORTED, 0));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

    public static class PointEvent {
        private int status;
        private double point;

        public PointEvent(int status, double point) {
            this.status = status;
            this.point = point;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getPoint() {
            return point;
        }

        public void setPoint(double point) {
            this.point = point;
        }
    }
}
