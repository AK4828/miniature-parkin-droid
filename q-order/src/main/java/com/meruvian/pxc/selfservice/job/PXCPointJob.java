package com.meruvian.pxc.selfservice.job;

import android.util.Log;

import com.meruvian.pxc.selfservice.SignageAppication;
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
 * Created by akm on 26/02/16.
 */
public class PXCPointJob extends Job {

    protected PXCPointJob() {
        super(new Params(1).requireNetwork().persist());
    }

    public static PXCPointJob newInstance() {

        PXCPointJob pxcPointJob = new PXCPointJob();

        return pxcPointJob;

    }
    @Override
    public void onAdded() {
        EventBus.getDefault().post(JobStatus.ADDED);
    }

    @Override
    public void onRun() throws Throwable {
        SignageAppication appication = SignageAppication.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("access_token", AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        try {
            PointService service = appication.getRetrofit().create(PointService.class);
            Call<Point> pointService = service.getUserPointPXC(param);
            pointService.enqueue(new Callback<Point>() {
                @Override
                public void onResponse(Response<Point> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Point point = response.body();
                        if (response.code() == 204) {
                            EventBus.getDefault().post(new PointEvent(JobStatus.USER_ERROR, 0));
                        } else if (response.code() == 200) {
                            Log.d("POINT", String.valueOf(point.getPoint()));
                            EventBus.getDefault().post(new PointEvent(JobStatus.SUCCESS, point.getPoint()));
                        }

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    EventBus.getDefault().post(new PointEvent(JobStatus.SYSTEM_ERROR, 0.0));

                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new PointEvent(JobStatus.ABORTED, 0.0));
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
