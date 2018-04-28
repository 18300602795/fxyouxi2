package com.etsdk.app.huov7.provider;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsdk.app.huov7.BuildConfig;
import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.model.TjColumnHead;
import com.etsdk.app.huov7.ui.CouponListActivity;
import com.etsdk.app.huov7.ui.GameListActivity;
import com.etsdk.app.huov7.ui.GiftCardListActivity;
import com.etsdk.app.huov7.ui.GiftListActivity;
import com.etsdk.app.huov7.ui.MainActivity;
import com.etsdk.hlrefresh.BaseRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by liu hong liang on 2016/12/21.
 */
public class TjColumnHeadViewProvider
        extends ItemViewProvider<TjColumnHead, TjColumnHeadViewProvider.ViewHolder> {
    boolean requestBackGroundColor;
    BaseRefreshLayout baseRefreshLayout;

    public TjColumnHeadViewProvider(BaseRefreshLayout baseRefreshLayout) {
        this.baseRefreshLayout = baseRefreshLayout;
    }

    public TjColumnHeadViewProvider() {
    }

    public boolean isRequestBackGroundColor() {
        return requestBackGroundColor;
    }

    public void setRequestBackGroundColor(boolean requestBackGroundColor) {
        this.requestBackGroundColor = requestBackGroundColor;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_tj_column_head, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull TjColumnHead tjColumnHead) {
        if (tjColumnHead.getType() == TjColumnHead.TYPE_NEW_GAME_SF) {
            holder.tvTypeName.setText("新游首发");
            holder.ivHint.setBackgroundResource(R.mipmap.smnews);
            holder.tvMore.setText("更多");
            holder.ivMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameListActivity.start(v.getContext(), holder.tvTypeName.getText().toString(), true, true, 0, 2, 0, 0, 0, 0, 0, null);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_GAME_WELFARE) {
            if (BuildConfig.projectCode == 177) {//177七二网络改叫bt游戏
                holder.tvTypeName.setText("BT游戏");
            } else {
                holder.tvTypeName.setText("公益游戏");
            }
            holder.ivHint.setBackgroundResource(R.mipmap.gongyi);
            holder.tvMore.setText("更多");
            holder.ivMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameListActivity.start(v.getContext(), holder.tvTypeName.getText().toString(), true, true, 0, 0, 0, 0, 0, 2, 0, null);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_GAME_FXB) {
            if (BuildConfig.projectCode == 137) {
                holder.tvTypeName.setText("BT游戏");
            } else {
                holder.tvTypeName.setText("热门游戏");
            }
            holder.ivHint.setBackgroundResource(R.mipmap.remen);
            holder.tvMore.setText("更多");
            holder.ivMore.setVisibility(View.VISIBLE);//跳转至游戏热门分类
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.itemView.getContext() instanceof MainActivity) {
//                        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
//                        mainActivity.switchGameFragment(1);
                        GameListActivity.start(v.getContext(), holder.tvTypeName.getText().toString(), true, true, 2, 0, 0, 0, 0, 0, 0, null);
                    }
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_GAME_TJ) {
            holder.tvTypeName.setText("新游推荐");
            holder.ivHint.setBackgroundResource(R.mipmap.zuixin);
            holder.tvMore.setText("更多");
            holder.ivMore.setVisibility(View.VISIBLE);//跳转至游戏新游
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.itemView.getContext() instanceof MainActivity) {
//                        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
//                        mainActivity.switchGameFragment(2);
                        GameListActivity.start(v.getContext(), holder.tvTypeName.getText().toString(), true, true, 0, 2, 0, 0, 0, 0, 0, null);
                    }
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_GAME_LIKE) {
            holder.tvTypeName.setText("猜你喜欢");
            holder.ivHint.setBackgroundResource(R.mipmap.xihuan);
            holder.tvMore.setText("换一批");
            holder.ivMore.setVisibility(View.GONE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseRefreshLayout != null) {
                        baseRefreshLayout.refresh();
                    }
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_LUXURY_GIFT) {
            holder.tvTypeName.setText("豪华礼包");
            holder.ivHint.setVisibility(View.GONE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(), "豪华礼包", null, 0, 0, 0, 2);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_TIME_COUPON) {
            holder.tvTypeName.setText("限时代金券");
            holder.ivHint.setVisibility(View.GONE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CouponListActivity.start(v.getContext(), "限时代金券", null, 0);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_HOT_REC_GIFT) {
            holder.tvTypeName.setText("热门推荐礼包");
            holder.tvTypeName.setTextColor(holder.itemView.getResources().getColor(R.color.bg_blue));
            holder.ivHint.setBackgroundResource(R.mipmap.rec_blue);
            holder.ivHint.setVisibility(View.VISIBLE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(), "热门推荐礼包", null, 0, 0, 2, 0);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_HOT_GIFT) {
            holder.tvTypeName.setText("热门礼包");
            holder.tvTypeName.setTextColor(holder.itemView.getResources().getColor(R.color.bg_blue));
            holder.ivHint.setBackgroundResource(R.mipmap.rec_blue);
            holder.ivHint.setVisibility(View.VISIBLE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(), "热门礼包", null, 2, 0, 0, 0);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_NEW_GIFT) {
            holder.tvTypeName.setText("最新礼包");
            holder.tvTypeName.setTextColor(holder.itemView.getResources().getColor(R.color.bg_blue));
            holder.ivHint.setBackgroundResource(R.mipmap.rec_blue);
            holder.ivHint.setVisibility(View.VISIBLE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftListActivity.start(v.getContext(), "最新礼包", null, 0, 2, 0, 0);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_SCORE_COUPONE) {
            holder.tvTypeName.setText("代金券兑换专区");
            holder.ivHint.setVisibility(View.GONE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CouponListActivity.start(v.getContext(), "代金券兑换专区", null, 0);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_SCORE_GIFT_CARD) {
            holder.tvTypeName.setText("礼品卡兑换专区");
            holder.ivHint.setVisibility(View.GONE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftCardListActivity.start(v.getContext(), "礼品卡兑换专区", 1);
                }
            });
        } else if (tjColumnHead.getType() == TjColumnHead.TYPE_SCORE_GOODS) {
            holder.tvTypeName.setText("实物兑换专区");
            holder.ivHint.setVisibility(View.GONE);
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GiftCardListActivity.start(v.getContext(), "实物兑换专区", 2);
                }
            });
        }
        if (requestBackGroundColor) {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.bg_gray));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_hint)
        ImageView ivHint;
        @BindView(R.id.tv_typeName)
        TextView tvTypeName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.iv_more)
        ImageView ivMore;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}