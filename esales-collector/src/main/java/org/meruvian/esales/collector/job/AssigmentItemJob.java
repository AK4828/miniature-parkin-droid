package org.meruvian.esales.collector.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.esales.collector.SignageAppication;
import org.meruvian.esales.collector.content.database.adapter.AssigmentDetailItemDatabaseAdapter;
import org.meruvian.esales.collector.entity.AssigmentDetailItem;
import org.meruvian.esales.collector.entity.OrderMenu;
import org.meruvian.esales.collector.entity.PageEntity;
import org.meruvian.esales.collector.event.GenericEvent;
import org.meruvian.esales.collector.util.JsonRequestUtils;
import org.meruvian.midas.core.job.Priority;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 12/09/15.
 */
public class AssigmentItemJob extends Job {
    public static final int PROCESS_ID = 51;
    private JsonRequestUtils.HttpResponseWrapper<PageEntity<OrderMenu>> response;
    private String url, agenId, asdId;

    public AssigmentItemJob(String url, String agenId, String asdId) {
        super(new Params(Priority.HIGH).requireNetwork().persist());
        this.url = url;
        this.agenId = agenId;
        this.asdId = asdId;
    }

    @Override
    public void onAdded() {
        Log.d(getClass().getSimpleName(), "onAdded");
        EventBus.getDefault().post(new GenericEvent.RequestInProgress(PROCESS_ID));
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(getClass().getSimpleName(), "onRun");
        JsonRequestUtils request = new JsonRequestUtils(new Formatter().format(
                url + CollectorUri.GET_ASG_ITEM_BY_AGENT, agenId).toString());
        response = request.get(new TypeReference<PageEntity<OrderMenu>>() {});

        for (int i = 0; i <= response.getContent().getTotalPages(); i++) {
            request.removeAllQueryParam();
            request.addQueryParam("page", String.valueOf(i));
            Log.d(getClass().getSimpleName(), "Pages :" + i + "/" + response.getContent().getTotalPages());

            response = request.get(new TypeReference<PageEntity<OrderMenu>>() {});

            HttpResponse r = response.getHttpResponse();

            if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                List<OrderMenu> oms = response.getContent().getContent();
                Log.d(getClass().getSimpleName(), "Response.OM.Size: " + oms.size());

                List<AssigmentDetailItem> asgDetailItems = new ArrayList<AssigmentDetailItem>();
                for (OrderMenu om : oms) {
                    AssigmentDetailItem ad = new AssigmentDetailItem();
                    ad.getAssigmentDetail().setId(asdId);
                    ad.setProduct(om.getProduct());
                    ad.setQty(om.getQty());

                    Log.d(getClass().getSimpleName(),
                            "Asgmnt ID: " + asdId + "\n" +
                            "Produk: " + om.getProduct().getProduct().getName() + "\n" +
                            "Qty: " + om.getQty() + "\n\n");

                    asgDetailItems.add(ad);
                }

                AssigmentDetailItemDatabaseAdapter asgItemDbAdapter =
                        new AssigmentDetailItemDatabaseAdapter(SignageAppication.getInstance());
                asgItemDbAdapter.saveAssigmentDetailItems(asgDetailItems);

            } else {
                Log.d(getClass().getSimpleName(), "2Response Code :" + r.getStatusLine().getStatusCode()
                        + " " + r.getStatusLine().getReasonPhrase());
                throw new RuntimeException();
            }
        }

        EventBus.getDefault().post(new GenericEvent.RequestSuccess(PROCESS_ID, response, "refId", agenId));
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new GenericEvent.RequestFailed(PROCESS_ID, response));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

}
