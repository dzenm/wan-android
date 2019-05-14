package com.din.wanandroid.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.din.helper.dialog.*;
import com.din.wanandroid.R;
import com.din.wanandroid.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn0) {
            new LoginDialog(this)
                    .setLoginByPassword()
                    .setOnClickListener(new LoginDialog.OnClickListener() {
                        @Override
                        public void onClick(LoginDialog dialog) {
                            Toast.makeText(TestActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerifeClick() {
                            Toast.makeText(TestActivity.this, "请求验证码", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build();
        } else if (view.getId() == R.id.btn1) {
            InfoDialog.newInstance(this)
                    .setContent("是否打开")
                    .setInfo("标题")
                    .setOnDialogClickListener(new InfoDialog.OnDialogClickListener<InfoDialog>() {
                        @Override
                        public void onClick(InfoDialog dialog, boolean confirm) {
                            if (confirm) {
                                Toast.makeText(TestActivity.this, "点击了确定", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build();
        } else if (view.getId() == R.id.btn2) {
            InfoDialog.newInstance(this)
                    .setInfo("标题")
                    .setContent("是否打开")
                    .setOnDialogClickListener(new InfoDialog.OnDialogClickListener<InfoDialog>() {
                        @Override
                        public void onClick(InfoDialog dialog, boolean confirm) {
                            if (confirm) {
                                Toast.makeText(TestActivity.this, "点击了确定", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .build();
        } else if (view.getId() == R.id.btn3) {
            InfoDialog.newInstance(this)
                    .setInfo("标题")
                    .setContent("是否打开")
                    .setOnDialogClickListener(new InfoDialog.OnDialogClickListener<InfoDialog>() {
                        @Override
                        public void onClick(InfoDialog dialog, boolean confirm) {
                            if (confirm) {
                                Toast.makeText(TestActivity.this, "点击了确定", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setGravity(Gravity.TOP)
                    .build();
        } else if (view.getId() == R.id.btn4) {
            EditDialog.newInstance(this)
                    .setInfo("测试")
                    .setContent("是否打开")
                    .setOnDialogClickListener(new EditDialog.OnDialogClickListener<EditDialog>() {
                        @Override
                        public void onClick(EditDialog dialog, boolean confirm) {
                            if (confirm) {
                                Toast.makeText(TestActivity.this, "点击了确定: " + dialog.getContent(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .build();
        } else if (view.getId() == R.id.btn5) {
            EditDialog.newInstance(this)
                    .setInfo("测试")
                    .setContent("是否打开")
                    .setOnDialogClickListener(new EditDialog.OnDialogClickListener<EditDialog>() {
                        @Override
                        public void onClick(EditDialog dialog, boolean confirm) {
                            if (confirm) {
                                Toast.makeText(TestActivity.this, "点击了确定: " + dialog.getContent(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .build();
        } else if (view.getId() == R.id.btn6) {
            EditDialog.newInstance(this)
                    .setInfo("测试")
                    .setContent("是否打开")
                    .setOnDialogClickListener(new EditDialog.OnDialogClickListener<EditDialog>() {
                        @Override
                        public void onClick(EditDialog dialog, boolean confirm) {
                            if (confirm) {
                                Toast.makeText(TestActivity.this, "点击了确定: " + dialog.getContent(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TestActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setGravity(Gravity.TOP)
                    .build();
        } else if (view.getId() == R.id.btn7) {
            final String[] texts = new String[]{"测试1", "测试2", "测试3", "测试4", "测试1", "测试2", "测试3", "测试4"};
            ListDialog.newInstance(this).setData(texts)
                    .setOnItemClickListener(new ListDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(TestActivity.this, texts[position] + ": " + position, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build();
        } else if (view.getId() == R.id.btn8) {
            final String[] texts = new String[]{"测试1", "测试2", "测试3", "测试4", "测试1", "测试2", "测试3", "测试4"};
            ListDialog.newInstance(this).setData(texts)
                    .setOnItemClickListener(new ListDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(TestActivity.this, texts[position] + ": " + position, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setGravity(Gravity.BOTTOM)
                    .build();
        } else if (view.getId() == R.id.btn9) {
            final String[] texts = new String[]{"测试1", "测试2", "测试3", "测试4", "测试1", "测试2", "测试3", "测试4"};
            ListDialog.newInstance(this).setData(texts)
                    .setOnItemClickListener(new ListDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(TestActivity.this, texts[position] + ": " + position, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setGravity(Gravity.TOP)
                    .build();
        } else if (view.getId() == R.id.btn11) {
            PromptDialog.newInstance(this)
                    .showLoadingPoint();
        } else if (view.getId() == R.id.btn12) {
            PromptDialog.newInstance(this)
                    .setGravity(Gravity.BOTTOM)
                    .showLoadingPoint();
        } else if (view.getId() == R.id.btn13) {
            PromptDialog.newInstance(this)
                    .setGravity(Gravity.TOP)
                    .showLoadingPoint();
        } else if (view.getId() == R.id.btn14) {
            PromptDialog.newInstance(this)
                    .showLoading();
        } else if (view.getId() == R.id.btn15) {
            PromptDialog.newInstance(this)
                    .setGravity(Gravity.BOTTOM)
                    .showLoading();
        } else if (view.getId() == R.id.btn16) {
            PromptDialog.newInstance(this)
                    .setGravity(Gravity.TOP)
                    .showLoading();
        } else if (view.getId() == R.id.btn17) {
            PromptDialog.newInstance(this)
                    .showSuccess("登录成功");
        } else if (view.getId() == R.id.btn18) {
            PromptDialog.newInstance(this)
                    .showError("禁止访问");
        } else if (view.getId() == R.id.btn19) {
            PromptDialog.newInstance(this)
                    .showWarming("您的身份信息可能会被泄露");
        } else if (view.getId() == R.id.btn20) {
            UpGradeDialog.newInstance(this)
                    .setOnUpGradeListener(new UpGradeDialog.OnUpGradeListener() {
                        @Override
                        public void onStart(LoadProgress loadProgress) {
                            l = loadProgress;

                            start();
                        }

                        @Override
                        public void onFinished() {
                            Toast.makeText(TestActivity.this, "升级完成，进行安装", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build();
        }
    }

    private LoadProgress l;
    private int max = 100; //总的大小
    private int current = 0; //当前下载大小

    //循环模拟下载过程
    public void start() {
        if (current <= max) {
            l.setCurrentValue(current);
            handler.postDelayed(runnable, 100);
        } else {
            current = 0;
            handler.removeCallbacks(runnable);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            current = current + 1;
            start();
        }
    };
}