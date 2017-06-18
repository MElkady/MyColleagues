package me.elkady.mycolleagues.mycolleagues;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.elkady.mycolleagues.R;
import me.elkady.mycolleagues.colleaguedetails.ColleagueDetailsActivity;
import me.elkady.mycolleagues.data.ColleaguesRepositoryImpl;
import me.elkady.mycolleagues.models.Colleague;

public class MyColleaguesActivity extends AppCompatActivity implements MyColleaguesContract.View {
    private MyColleaguesContract.Presenter mPresenter;
    private List<Colleague> mRecentColleagues;
    private List<Colleague> mOtherColleagues;

    @BindView(R.id.rv_colleagues) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_colleagues);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mPresenter = new MyColleaguesPresenter(new ColleaguesRepositoryImpl());
        mPresenter.attachView(this);
        mPresenter.loadColleagues();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void setColleagues(List<Colleague> recentColleagues, List<Colleague> otherColleagues) {
        mRecentColleagues = recentColleagues;
        mOtherColleagues = otherColleagues;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(@StringRes int errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mycolleagues, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item_clear) {
            mPresenter.reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final long HOUR = 1000 * 60 * 60;
    private static final long DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long MONTH = DAY * 30;

    private Spanned getLastInteractionMessage(Colleague colleague) {
        long now = System.currentTimeMillis();
        long diff = now - colleague.getLastInteractionTime();

        if(colleague.getLastInteractions() == null || colleague.getLastInteractions().size() < 1) {
            return Html.fromHtml(getString(R.string.last_feedback_never));
        }

        if(colleague.isRecentlyInteracted()) {
            if (diff > DAY) {
                long noDays = diff / DAY;
                return Html.fromHtml(getString(R.string.last_feedback_days_ago, noDays));
            } else if(diff > HOUR) {
                long noHours = diff / HOUR;
                return Html.fromHtml(getString(R.string.last_feedback_hours_ago, noHours));
            } else {
                return Html.fromHtml(getString(R.string.last_feedback_less_than_hour_ago));
            }
        } else {
            if (diff > MONTH) {
                long noMonths = diff / MONTH;
                return Html.fromHtml(getString(R.string.last_feedback_months_ago, noMonths));
            } else if(diff > WEEK) {
                long noWeeks = diff / WEEK;
                return Html.fromHtml(getString(R.string.last_feedback_weeks_ago, noWeeks));
            } else {
                long noDays = diff / DAY;
                return Html.fromHtml(getString(R.string.last_feedback_days_ago, noDays));
            }
        }
    }

    class RecentColleagueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar) ImageView ivAvatar;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_details) TextView tvDetails;

        RecentColleagueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(final Colleague colleague) {
            tvName.setText(colleague.getName());
            Picasso.with(MyColleaguesActivity.this).load(colleague.getAvatar()).placeholder(R.mipmap.ic_launcher).into(ivAvatar);
            tvDetails.setText(getLastInteractionMessage(colleague));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ColleagueDetailsActivity.createIntent(MyColleaguesActivity.this, colleague.getId()));
                }
            });
        }
    }

    class OtherColleagueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar) ImageView ivAvatar;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_details) TextView tvDetails;
        @BindView(R.id.btn_give_feedback) Button btnGiveFeedback;

        OtherColleagueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(final Colleague colleague) {
            tvName.setText(colleague.getName());
            Picasso.with(MyColleaguesActivity.this).load(colleague.getAvatar()).placeholder(R.mipmap.ic_launcher).into(ivAvatar);
            tvDetails.setText(getLastInteractionMessage(colleague));
            btnGiveFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.giveFeedback(colleague);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ColleagueDetailsActivity.createIntent(MyColleaguesActivity.this, colleague.getId()));
                }
            });
        }
    }

    private SimpleSectionedAdapter<RecyclerView.ViewHolder> mAdapter = new SimpleSectionedAdapter<RecyclerView.ViewHolder>() {
        private static final int OTHER_COLLEAGUES = 0;
        private static final int RECENT_COLLEAGUES = 1;

        @Override
        protected String getSectionHeaderTitle(int section) {
            switch (section) {
                case OTHER_COLLEAGUES:
                    return getString(R.string.give_them_some_feedback);
                case RECENT_COLLEAGUES:
                    return getString(R.string.you_gave_them_feedback_recently);
            }
            return null;
        }

        @Override
        protected int getSectionCount() {
            return 2;
        }

        @Override
        protected int getItemCountForSection(int section) {
            switch (section) {
                case OTHER_COLLEAGUES:
                    return (mOtherColleagues != null)? mOtherColleagues.size() : 0;
                case RECENT_COLLEAGUES:
                    return (mRecentColleagues != null)? mRecentColleagues.size() : 0;
            }
            return 0;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case OTHER_COLLEAGUES:
                    return new OtherColleagueViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_other_colleague, parent, false));
                case RECENT_COLLEAGUES:
                    return new RecentColleagueViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_colleague, parent, false));
            }
            return null;
        }

        @Override
        protected int getSectionItemViewType(int section, int position) {
            return section;
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int section, int position) {
            switch (section) {
                case OTHER_COLLEAGUES:
                    OtherColleagueViewHolder recentColleagueViewHolder = (OtherColleagueViewHolder) holder;
                    recentColleagueViewHolder.bindView(mOtherColleagues.get(position));
                    break;
                case RECENT_COLLEAGUES:
                    RecentColleagueViewHolder otherColleagueViewHolder = (RecentColleagueViewHolder) holder;
                    otherColleagueViewHolder.bindView(mRecentColleagues.get(position));
                    break;
            }
        }
    };
}
