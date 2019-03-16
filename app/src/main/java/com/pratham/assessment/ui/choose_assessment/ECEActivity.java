package com.pratham.assessment.ui.choose_assessment;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.pratham.assessment.R;
import com.pratham.assessment.discrete_view.DSVOrientation;
import com.pratham.assessment.discrete_view.DiscreteScrollView;
import com.pratham.assessment.discrete_view.ScaleTransformer;
import com.pratham.assessment.domain.ECEModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ECEActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener, AnswerClickedListener {
    @BindView(R.id.attendance_recycler_view)
    DiscreteScrollView discreteScrollView;
    List<ECEModel> eceModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ece);
        ButterKnife.bind(this);

        JSONArray jsonArray = fetchJson("ece.json");
        eceModelList=parsejsonArray(jsonArray);
        //insertJsonToDB(jsonArray);


        ECEAdapter eceAdapter = new ECEAdapter(this,eceModelList);
        discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
        discreteScrollView.addOnItemChangedListener(this);
        discreteScrollView.setItemTransitionTimeMillis(200);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.5f)
                .build());
        discreteScrollView.setAdapter(eceAdapter);
        eceAdapter.notifyDataSetChanged();

    }

    private List<ECEModel> parsejsonArray(JSONArray jsonArray) {
        List<ECEModel> eceList = new ArrayList<>();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                ECEModel eceModel = new ECEModel();
                eceModel.setQuestionId(jsonArray.getJSONObject(i).getString("questionId"));
                eceModel.setQuestion(jsonArray.getJSONObject(i).getString("question"));
                eceModel.setType(jsonArray.getJSONObject(i).getString("type"));
                eceModel.setTitle(jsonArray.getJSONObject(i).getString("title"));
                eceModel.setInstructions(jsonArray.getJSONObject(i).getString("instructions"));
                eceModel.setVideo(jsonArray.getJSONObject(i).getString("video"));
                eceModel.setRating(jsonArray.getJSONObject(i).getString("rating"));
                eceList.add(eceModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eceList;
    }

    /*private void insertJsonToDB(JSONArray jsonArray) {
        List<ECEModel> eceModelArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < eceModelArrayList.length(); i++) {
                WordEnglish sentence = new WordEnglish();
                sentence.setWord(englishSentences.getJSONObject(i).getString("data"));
                sentence.setUuid(englishSentences.getJSONObject(i).getString("resourceId"));
                sentence.setSize(englishSentences.getJSONObject(i).getInt("wordCount"));
                sentence.setType("sentence");

                EnglishSentenceList.add(sentence);
            }
            AppDatabase.getDatabaseInstance(context).getEnglishWordDao().insertWordList(EnglishSentenceList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/
    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    public JSONArray fetchJson(String jasonName) {
        JSONArray jsonArr = null;
        try {
            //InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/RC/" + jasonName);
            InputStream is = this.getAssets().open("" + jasonName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            jsonArr = new JSONArray(jsonStr);
            //returnStoryNavigate = jsonObj.getJSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArr;
    }

    @Override
    public void onAnswerClicked(int position, int answer) {
        if(answer==1){

        }
    }
}
