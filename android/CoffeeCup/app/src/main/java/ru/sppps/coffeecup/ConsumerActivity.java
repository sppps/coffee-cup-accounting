package ru.sppps.coffeecup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConsumerActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mDebtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        mNameView = (EditText)findViewById(R.id.consumer_name);
        mDebtView = (EditText)findViewById(R.id.consumer_debt);
        if (getIntent().getAction() == Intent.ACTION_EDIT) {
            setTitle(R.string.edit_consumer_title);
            mNameView.setText(getIntent().getStringExtra("name"));
            mDebtView.setText(String.format("%f", getIntent().getDoubleExtra("debt", 0.0)));
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
            getWindow().setEnterTransition(transition);
        } else {
            setTitle(R.string.add_consumer_title);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button clickButton = (Button) findViewById(R.id.add_consumer_btn);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mNameView.getText().length() < 3) {
                    mNameView.setError(getString(R.string.msg_enter_consumer_name));
                    return;
                }

                Intent data = new Intent();
                String text = "Result to be returned....";
                data.setData(Uri.parse(text));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                overridePendingTransition(R.anim.enter_back, R.anim.exit_back);
                return true;
        }
        return false;
    }
}
