package me.elkady.mycolleagues.colleaguedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.elkady.mycolleagues.R;
import me.elkady.mycolleagues.data.ColleaguesRepositoryImpl;
import me.elkady.mycolleagues.models.Colleague;
import me.elkady.mycolleagues.models.Interaction;

public class ColleagueDetailsActivity extends AppCompatActivity implements ColleagueDetailsContract.View {
    private static final String ARG_Colleague_ID = "colleague_id";

    private Colleague mColleague;
    private ColleagueDetailsContract.Presenter mPresenter;

    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_section_title) TextView tvSectionTitle;
    @BindView(R.id.rv_feedbacks) RecyclerView rvFeedbacks;
    @BindView(R.id.iv_avatar) ImageView ivAvatar;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static Intent createIntent(Context context, int colleagueId) {
        Intent i = new Intent(context, ColleagueDetailsActivity.class);
        i.putExtra(ARG_Colleague_ID, colleagueId);
        context.startActivity(i);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleague_details);

        if(!getIntent().hasExtra(ARG_Colleague_ID)) {
            finish();
            return;
        }

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int colleageId = getIntent().getIntExtra(ARG_Colleague_ID, 0);

        mPresenter = new ColleagueDetailsPresenter(new ColleaguesRepositoryImpl());
        mPresenter.attachView(this);
        mPresenter.getColleagueById(colleageId);

        rvFeedbacks.setAdapter(mAdapter);
        rvFeedbacks.setLayoutManager(new LinearLayoutManager(ColleagueDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        rvFeedbacks.addItemDecoration(new DividerItemDecoration(ColleagueDetailsActivity.this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void displayColleague(Colleague colleague) {
        mColleague = colleague;
        tvName.setText(colleague.getName());
        Picasso.with(ColleagueDetailsActivity.this).load(colleague.getAvatar()).placeholder(R.mipmap.ic_launcher).into(ivAvatar);
        tvSectionTitle.setText(getString(R.string.feedback_given_to_x, colleague.getName().split(" ")[0]));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(@StringRes int errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private static final long HOUR = 1000 * 60 * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long MONTH = DAY * 30;

    class InteractionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_details) TextView textView;
        public InteractionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(Interaction interaction) {
            long now = System.currentTimeMillis();
            long diff = now - interaction.getDate().getTime();

            if (diff > MONTH) {
                long noMonths = diff / MONTH;
                textView.setText(Html.fromHtml(getString(R.string.sent_feedback_months_ago, noMonths)));
            } else if(diff > WEEK) {
                long noWeeks = diff / WEEK;
                textView.setText(Html.fromHtml(getString(R.string.sent_feedback_weeks_ago, noWeeks)));
            } else if (diff > DAY) {
                long noDays = diff / DAY;
                textView.setText(Html.fromHtml(getString(R.string.sent_feedback_days_ago, noDays)));
            } else if(diff > HOUR) {
                long noHours = diff / HOUR;
                textView.setText(Html.fromHtml(getString(R.string.sent_feedback_hours_ago, noHours)));
            } else {
                textView.setText(Html.fromHtml(getString(R.string.sent_feedback_less_than_hour_ago)));
            }
        }
    }

    private RecyclerView.Adapter<InteractionViewHolder> mAdapter = new RecyclerView.Adapter<InteractionViewHolder>() {
        @Override
        public InteractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InteractionViewHolder(LayoutInflater.from(ColleagueDetailsActivity.this).inflate(R.layout.item_interaction_record, parent, false));
        }

        @Override
        public void onBindViewHolder(InteractionViewHolder holder, int position) {
            holder.bindView(mColleague.getLastInteractions().get(position));
        }

        @Override
        public int getItemCount() {
            return (mColleague != null && mColleague.getLastInteractions() != null)? mColleague.getLastInteractions().size() : 0;
        }
    };
}
