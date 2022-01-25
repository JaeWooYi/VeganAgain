package com.example.test.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.R;
import com.example.test.view.DataItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class RecipeActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;

    int count;
    Context cThis;//context 설정
    String LogTT = "[STT]";//LOG타이틀
    String[] sttresult = null;
    //음성 인식용
    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    //음성 출력용
    TextToSpeech tts;

    ArrayList<String> soup = new ArrayList<String>(Arrays.asList("찌개", "된장찌개", "김치찌개", "동태찌개", "황태찌개", "부대찌개", "알탕", "김치찌개 레시피"));
    //일단내가주석처리해놓음(재우)
    //ArrayList<String> soup = new ArrayList<>(Arrays.asList("찌개", "된장찌개", "김치찌개", "동태찌개", "황태찌개", "부대찌개", "알탕", "김치찌개 레시피"));
    // 화면 처리용

    Button btnSttStart, recBtn;
    EditText txtInMsg;
    ArrayList<String> mResult = new ArrayList<String>();           //객체생성해놓음 (재우)
    ArrayList<String> fileList;
    ArrayList<DataItem> fileList_rv;
    ArrayList<String> exceptList;
    private ListView listview;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    private int limit = 10000;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    ArrayList<RecipeVO> postList;
    private DocumentSnapshot lastVisible;
    String[] rs;
    boolean sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        Context context = getApplicationContext();

        Log.d("ddd", "onCreate");

        recyclerView = findViewById(R.id.foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), 1));

        postList = new ArrayList<RecipeVO>();
        productAdapter = new ProductAdapter(postList);
        recyclerView.setAdapter(productAdapter);

        fileList = new ArrayList<>();
        fileList_rv = new ArrayList<>();
        recyclerView = findViewById(R.id.foodList);
        // listview = (ListView) findViewById(R.id.foodList);
        // adapter = new ArrayAdapter<String>(this, R.layout.activity_search_textview,fileList);
        // listview.setAdapter(adapter);

        cThis = this;
        //음성인식
        SttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");//한국어 사용
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(cThis);
        mRecognizer.setRecognitionListener(listener);
        firestore = FirebaseFirestore.getInstance();

        txtInMsg = (EditText) findViewById(R.id.txtInMsg);
        txtInMsg.setTextIsSelectable(true);

        ArrayList<String> soup = new ArrayList<>(Arrays.asList("찌개", "된장찌개", "김치찌개", "동태찌개", "황태찌개", "부대찌개", "알탕", "김치찌개 레시피"));

        //음성출력 생성, 리스너 초기화
        tts = new TextToSpeech(cThis, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        //버튼설정
        btnSttStart = (Button) findViewById(R.id.btn_stt_start);
        btnSttStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("음성인식 시작!");

                if (ContextCompat.checkSelfPermission(cThis, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    //ActivityCompat.requestPermissions(testSearch, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    //권한을 허용하지 않는 경우
                } else {
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(SttIntent);

                        CollectionReference reference = firestore.collection("KFD");

                        System.out.println("황 getCollection" + reference);

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        recBtn = (Button) findViewById(R.id.recommand_button);

        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cThis, "검색 중입니다... 기다려 주세요(●'◡'●)", Toast.LENGTH_SHORT).show();
                CollectionReference reference = firestore.collection("KFD");

                System.out.println("황 getCollection" + reference);

//                        Log.i("닭갈비 확인",txtInMsg.getText().toString());

//        Log.d("ddd", "getCollection "+reference);

//        Query query = reference.orderBy("id"); // 이부분을 바꿔아햠

                Query query = reference.limit(limit);

                System.out.println(query.get());
//         in Python find(txtInMsg) != -1
//
//         query : id 1~3 -> 순서대로 저장.
//         for 문을 돌려서 하나씩 reference 안에 대입을해서
//         그 값을 받을 수 있음
//         id값 (json) id, image, title 이 세개를 객체로 받는데 객체로 받기 위해서는 json받아서 map 으로 처리

                RecyclerView finalRecyclerView = recyclerView;

                Task<QuerySnapshot> querySnapshotTask = query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        postList.clear();
                        if (task.isSuccessful()) {
                            if (task.getResult().size() <= 0) {
                                System.out.print("데이터 없음");
                            } else {
                                if (txtInMsg.getText().toString().trim().length() <= 0) {
                                    Toast.makeText(cThis, "입력을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    System.out.println("조개 " + txtInMsg.getText().toString());
                                    sc = false;
                                    count = 0;
                                    int count2 = 0;
                                    for (DocumentSnapshot document : task.getResult()) {
                                        count2++;
                                        if (document.getData().get("Name").toString().replace(" ", "").contains(txtInMsg.getText().toString().trim())) {

                                            postList.add(new RecipeVO(
                                                    document.getData().get("Name").toString(),
                                                    document.getData().get("CAR").toString(),
                                                    document.getData().get("PRO").toString(),
                                                    document.getData().get("FAT").toString(),
                                                    document.getData().get("KCAL").toString()));
                                            count++;
                                        }
                                        if (count2 == limit) {
                                            sc = true;
                                        }
                                    }
                                    if (count == 0) {
                                        Toast.makeText(cThis, "밑으로 스크롤 해주세요 👇", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                            productAdapter.notifyDataSetChanged();
                            lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);

                            RecyclerView.OnScrollListener OnScrollListener = new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                        isScrolling = true;
                                    }
                                }


                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                    int visibleItemCount = linearLayoutManager.getChildCount();
                                    int totalItemCount = linearLayoutManager.getItemCount();
                                    if (sc == true && isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                        isScrolling = false;
                                        sc = false;
                                        Query nextQuery = reference.startAfter(lastVisible).limit(limit);
                                        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                                if (t.getResult().size() > 0) {
                                                    if (t.isSuccessful()) {
                                                        count = 0;
                                                        for (DocumentSnapshot document : t.getResult()) {
                                                            count++;
                                                            if (document.getData().get("Name").toString().replace(" ", "").contains(txtInMsg.getText().toString().trim())) {
                                                                postList.add(new RecipeVO(
                                                                        document.getData().get("Name").toString(),
                                                                        document.getData().get("CAR").toString(),
                                                                        document.getData().get("PRO").toString(),
                                                                        document.getData().get("FAT").toString(),
                                                                        document.getData().get("KCAL").toString()));
                                                            }
                                                            if (count == limit) {
                                                                sc = true;
                                                            }
                                                        }
                                                        if (count == 0) {
                                                            Toast.makeText(cThis, "밑으로 스크롤 해주세요 👇", Toast.LENGTH_SHORT).show();
                                                        }

                                                        productAdapter.notifyDataSetChanged();
                                                        lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                                        if (t.getResult().size() < limit) {
                                                            isLastItemReached = true;
                                                            Toast.makeText(cThis, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            };
                            recyclerView.addOnScrollListener(OnScrollListener);
                        }
                        Log.d("ddd", "onComplete");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d("ddd", "msg : " + e.getMessage());
                    }
                });
            }
        });
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
            txtInMsg.setText(null);
            System.out.println("onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            System.out.println("onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float v) {
            System.out.println("onRmsChanged");
            // 들리는 소리가 변경되었을 때 호출. 소리 단위는 dB(데시벨) 단위로 전달됨. onBufferReceived하고 비슷하게 호출된다.
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            System.out.println("onBufferReceived");
            // 새 소리가 들어왔을 때 호출. onReadyForSpeech과 onEndOfSpeech 사이에서 무수히 호출된다.
        }

        @Override
        public void onEndOfSpeech() {
            System.out.println("onEndOfSpeech");
            // 음성이 끝났을 때 호출. 음성 인식이 성공했다는 건 아니고 이 콜백 다음에 인식 결과에 따라 onError나 onResults가 호출된다.
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            System.out.println("onResults");

            String key = SpeechRecognizer.RESULTS_RECOGNITION;
            mResult = results.getStringArrayList(key);

            rs = new String[mResult.size()];
            mResult.toArray(rs);
            sttresult = rs;
//            Toast.makeText(getApplicationContext(), rs[0], Toast.LENGTH_SHORT).show();
            txtInMsg.setText(rs[0] + "\r\n" + txtInMsg.getText());
            System.out.println("대답결과 : " + txtInMsg.getText().toString());
            FuncVoiceOrderCheck(rs[0]);

        }


        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    //입력된 음성 메세지 확인 후 동작 처리
    private void FuncVoiceOrderCheck(String VoiceMsg) {
        if (VoiceMsg.length() < 1) return;

        VoiceMsg = VoiceMsg.replace(" ", "");//공백제거

        if (VoiceMsg.indexOf("카카오톡") > -1 || VoiceMsg.indexOf("카톡") > -1) {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
            startActivity(launchIntent);
            onDestroy();
        }//카카오톡 어플로 이동
        if (VoiceMsg.indexOf("전등꺼") > -1 || VoiceMsg.indexOf("불꺼") > -1) {
            FuncVoiceOut("전등을 끕니다");//전등을 끕니다 라는 음성 출력
        }
    }


    //음성 메세지 출력용
    private void FuncVoiceOut(String OutMsg) {
        if (OutMsg.length() < 1) return;

        tts.setPitch(1.0f);//목소리 톤1.0
        tts.setSpeechRate(1.0f);//목소리 속도
        tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);

        //어플이 종료할때는 완전히 제거

    }

    //카톡으로 이동을 했는데 음성인식 어플이 종료되지 않아 계속 실행되는 경우를 막기위해 어플 종료 함수
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }
    }

    public void myToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }


}

