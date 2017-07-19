package netsoft;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author chenp
 * @version 2017-07-14 11:35
 */

public class TestHttpClient extends HttpClient {

    public TestHttpClient(BaseApiInterface apiImpi) {
        super(apiImpi);
    }

    @NotNull
    @Override
    public Subscription doRequest(Object params, String tag) {
        if (params == null){
            switch (tag){
                case "DD":
                    return onResonseTagDD();
            }
        }else if (params instanceof RequestB){

            if (tag == null)
                return onResponse((RequestB) params);

            switch (tag){
                case "BA":
                    return onResponseTagBA((RequestB) params);
                case "BB":
                    return onReponseTagBB((RequestB) params);
            }
        }else if (params instanceof RequestA){
            if (tag == null)
                return onResponse((RequestA) params);
        }

        //未找到匹配的方法
        return null;
    }

    private Subscription onResponse(RequestA requestA){
        return doGet(requestA, "", RequestA.class, true, new ResponseCallBack() {
            @Override
            public void run(Object response) {

            }
        });
    }

    private Subscription onResponse(RequestB requestB){

    }

    private Subscription onReponseTagBB(RequestB requestB){

    }

    private Subscription onResponseTagBA(RequestB requestB){

    }

    private Subscription onResonseTagDD(){

    }

    interface ResponseCallBack{
        void run(Object response);
    }

    private <R, T> Subscription doGet(final T request, final String url, final Class<R> rClass, boolean isSubSchedule, final ResponseCallBack callback){
        return Observable.just(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<T, Observable<String>>() {
                    @Override
                    public Observable<String> call(T t) {
                        Map<String, String> obj = NetSoft.beanToMap(t);

                        return getApiImpi().doGet(url, obj);
                    }
                })
                .map(new Func1<String, R>() {
                    @Override
                    public R call(String s) {
                        return NetSoft.jsonToBean(s, rClass);
                    }
                })
                .observeOn(isSubSchedule ? Schedulers.io() : AndroidSchedulers.mainThread())
                .doOnNext(new Action1<R>() {
                    @Override
                    public void call(R r) {
                        callback.run(r);
                    }
                }).subscribe();
    }
}
