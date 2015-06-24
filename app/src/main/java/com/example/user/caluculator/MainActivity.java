package com.example.user.caluculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends Activity implements View.OnClickListener {
    private final int MENU_ID1 = Menu.FIRST;
    private final int MENU_ID2 = Menu.FIRST + 1;

    Button calcButton[];
    int calcId[] = {R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.plus, R.id.minus, R.id.multiplied, R.id.devide, R.id.equal, R.id.clear};//id配列calcId[14]

    private final int KEY_0 = 0;
    private final int KEY_1 = 1;
    private final int KEY_2 = 2;
    private final int KEY_3 = 3;
    private final int KEY_4 = 4;
    private final int KEY_5 = 5;
    private final int KEY_6 = 6;
    private final int KEY_7 = 7;
    private final int KEY_8 = 8;
    private final int KEY_9 = 9;
    private final int KEY_PLUS = 10;
    private final int KEY_MINUS = 11;
    private final int KEY_MULTIPLIED = 12;
    private final int KEY_DVD = 13;
    private final int KEY_EQUAL = 14;
    private final int KEY_CLEAR = 15;//キーをint型として割り当て。配列同様0から始まって14で終わる。

    TextView text;//結果をテキスト表示
    TextView text2;//途中式

    int beforeStatus = 0; //前処理。
    String nowValueA = String.valueOf(0);//初期値。演算子を入れる前の数値
    String nowValueB = String.valueOf(0);//初期値。演算子を入れた後の数値
    double sum = 0;//合計の初期化
    DecimalFormat format = new DecimalFormat("0.########");//小数点を表示するかどうか。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.display);//表示用TextView
        calcButton = new Button[calcId.length];//ボタン割り当て
        text2 = (TextView) findViewById(R.id.Review);

        for (int i = 0; i < calcId.length; i++) {
            calcButton[i] = (Button) findViewById(calcId[i]); //idと変数の割り当て
            calcButton[i].setOnClickListener(this);//イベント処理
        }
    }

    //終了確認
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.exit);
                alertDialogBuilder.setMessage(R.string.exit_text);
                alertDialogBuilder.setPositiveButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialogBuilder.setCancelable(true);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        menu.add(0,MENU_ID1,0,getString(R.string.setting));
        menu.add(0,MENU_ID2,1,getString(R.string.action_exit));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case MENU_ID1:

                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < calcId.length; i++) {
            //キーと配列の値が一致（ボタン判定）
            if (v.equals(calcButton[i])) {
                //CLEARの場合
                if (i == KEY_CLEAR) {
                    text.setText("0");
                    text2.setText("0");
                    sum = 0;//初期化
                    nowValueA = String.valueOf(0);//初期化
                    nowValueB = String.valueOf(0);//初期化
                    beforeStatus = KEY_CLEAR;
                }
                //＋を押した場合
                else if (i == KEY_PLUS || i == KEY_MINUS || i == KEY_MULTIPLIED || i == KEY_DVD) {
                    nowValueA = text.getText().toString();//TextViewの値をnowValueAに文字列として取得
                    int valueA = Integer.parseInt(nowValueA);//文字列をint型に変換
                    sum = valueA;//valueAの値をsumにストックする。

                    //状態遷移と途中式の符号表示
                    if (i == KEY_PLUS) {
                        beforeStatus = KEY_PLUS;
                        nowValueA = nowValueA + "+";
                    } else if (i == KEY_MINUS) {
                        beforeStatus = KEY_MINUS;
                        nowValueA = nowValueA + "-";
                    } else if (i == KEY_MULTIPLIED) {
                        beforeStatus = KEY_MULTIPLIED;
                        nowValueA = nowValueA + "×";
                    } else {
                        beforeStatus = KEY_DVD;
                        nowValueA = nowValueA + "÷";
                    }
                    text2.setText(nowValueA);
                }
                //＝を押した場合
                else if (i == KEY_EQUAL) {
                    int valueB = Integer.parseInt(nowValueB);//valueBをint型に変換
                    //状態遷移が＋の場合。
                    if (beforeStatus == KEY_PLUS) {
                        sum = sum + valueB;//加算処理
                    } else if (beforeStatus == KEY_MINUS) {
                        sum = sum - valueB;//減算処理
                    } else if (beforeStatus == KEY_MULTIPLIED) {
                        sum = sum * valueB;//乗算処理
                    } else if (beforeStatus == KEY_DVD) {
                        //0での除算かどうか。
                        if (valueB == 0) {
                            Toast.makeText(this, R.string.zero, Toast.LENGTH_LONG).show();
                        } else {
                            sum = sum / valueB;//除算処理
                        }
                    } else if (beforeStatus == KEY_EQUAL) {
                        sum = 0;
                    }
                    if (format.format(sum).length() > 10) {
                        sum = 0;
                        Toast.makeText(this, R.string.overdigit, Toast.LENGTH_LONG).show();//表示桁数オーバー
                    }
                    beforeStatus = KEY_EQUAL;
                    nowValueB = String.valueOf(0);//演算子を入れた後の数値を初期化
                    text.setText(format.format(sum));//合計値を文字列に戻す。
                    text2.setText(format.format(sum));//合計値を文字列に戻す。
                }
                //0～9の数字
                if (i < 10) {
                    //状態遷移が＋または－の場合
                    if (beforeStatus == KEY_PLUS || beforeStatus == KEY_MINUS || beforeStatus == KEY_MULTIPLIED || beforeStatus == KEY_DVD) {
                        //nowValueBが0のとき
                        if (nowValueB.equals("0")) {
                            nowValueB = String.valueOf(i);//iの値を新たにnowValueBとして設定する
                        } else if (nowValueB.length() > 9) {
                        } else {
                            nowValueB = nowValueB + i;//文字列nowValueにiの値を加える
                        }
                        text.setText(nowValueB);//nowValueBの値を出力する。
                        text2.setText(nowValueA + nowValueB);
                    }
                    //それ以外の状態遷移の場合
                    else {
                        nowValueA = text.getText().toString();//TextViewの値をnowValueAに文字列として取得
                        //nowValueAが0のとき
                        if (nowValueA.equals("0") || beforeStatus == KEY_EQUAL) {
                            nowValueA = String.valueOf(i);//iの値を新たに文字列として設定する。
                        } else if (nowValueA.length() > 9) {
                        } else {
                            nowValueA = nowValueA + i;
                        }
                        text.setText(nowValueA);
                        text2.setText(nowValueA);
                        beforeStatus = i;
                    }
                    break;
                }
            }
        }
    }
}




