package com.example.junhee.rxandroidbasic02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class MainActivity extends AppCompatActivity {

    public ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<String> data = new ArrayList<>();
    private TextView mTextView;
    private Button mBtnJust;
    private Button mBtnFrom;
    private Button mBtnDefer;
    private List<Memo> fromData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        setAdapter();
        initObservable();
        onItemSelected();
    }

    /* Observable 초기화 */
    Observable<String> forFrom;
    Observable<Memo> forJust;
    Observable<String> forDefer;

    private void initObservable() {

        /* Observable.from()을 사용하여 Observable 객체 생성 */
        forFrom = Observable.fromArray("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "hhh", "asjdflk", "sjdklfjsdalkfjlk");

        // 아래 데이터 셋팅을 위해 메모 객체를 생성한다.
        Memo memo1 = new Memo("1 번");
        Memo memo2 = new Memo("2 번");
        Memo memo3 = new Memo("3 번");
        Memo memo4 = new Memo("4 번");
        Memo memo5 = new Memo("5 번");

        /* Observable.just()을 사용하여 Observable 객체 생성 */
        forJust = Observable.just(memo1, memo2, memo3, memo4, memo5);

        /* Observable.defer()을 사용하여 Observable 객체 생성 */
        // defer의 경우, 매개변수로 '함수'를 받는다. (JavaScript function 넘기는 것과 비슷한 모습)
       forDefer = Observable.defer(new Callable<ObservableSource<? extends String>>() {
           @Override
           public ObservableSource<? extends String> call() throws Exception {
               return Observable.just("월요일", "화요일", "수요일");
           }
       });
    }


    /* subcribe(); 메소드를 통해 Observable을 구독한다. */
    // XML onClick에 바인드 되어 있음

    /**
     * onNext : Observable로 부터 넘겨 받은 데이터 처리에 대한 로직 작성
     * onError : 에러에 대한 로직 작성 혹은 에러 정보를 알려줄 수 있는 메시지 작성
     * onComplete : Observable로 부터 모든 데이터를 넘겨 받은 후의 작업 로직 작성
     */
    
    public void doFrom(View view) {

        if (data.size() > 0) {
            data.clear();
        }
        forFrom.subscribe(
                str -> data.add(str),                       // observable(발행자 : emitter)로부터 데이터를 가져온다..
                t -> {/* 일단 아무것도 안한다..*/},
                () -> {
                    adapter.notifyDataSetChanged();
                    mTextView.setText(data.size() + "");    // 완료되면 리스트에 알린다..
                }
        );
    }

    public void doJust(View view) {
        if (data.size() > 0) {
            data.clear();
        }
        forJust.subscribe(
                obj -> data.add(obj.content),
                t -> {/* 일단 아무것도 안한다..*/},
                () -> {
                    adapter.notifyDataSetChanged();
                    mTextView.setText(data.size() + "");
                }
        );
    }

    public void doDiffer(View view) {
        if (data.size() > 0) {
            data.clear();
        }
        forFrom.subscribe(
                str -> data.add(str),                       // observable(발행자 : emitter)로부터 데이터를 가져온다..
                t -> {/* 일단 아무것도 안한다..*/},
                () -> {
                    adapter.notifyDataSetChanged();
                    mTextView.setText(data.size() + "");    // 완료되면 리스트에 알린다..
                }
        );

    }

    /* 예제를 위한 셋팅 */
    private void setAdapter() {
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
    }

    private void onItemSelected() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), "[ " + adapterView.getItemAtPosition(i) + " ] 눌림", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.result);
        mBtnJust = (Button) findViewById(R.id.btnJust);
        mBtnFrom = (Button) findViewById(R.id.btnFrom);
        mBtnDefer = (Button) findViewById(R.id.btnDiffer);
        mListView = (ListView) findViewById(R.id.listView);
    }

    /* 예제를 위해 메모 클래스 작성 */
    private class Memo {

        String content;

        public Memo(String memo) {
            this.content = memo;
        }


    }
}

