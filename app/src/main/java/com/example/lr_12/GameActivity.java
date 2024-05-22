package com.example.lr_12;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[] buttons = new Button[9];
    private TextView statusText;
    private boolean isXTurn = true;
    private int[][] board = new int[3][3];
    private final int EMPTY = 0;
    private final int X = 1;
    private final int O = 2;
    private Handler handler = new Handler();
    private int buttonColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Получить выбранный цвет из SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String selectedColor = preferences.getString("selectedColor", "Red");
        buttonColor = getColor(selectedColor);

        buttons[0] = findViewById(R.id.btn00);
        buttons[1] = findViewById(R.id.btn01);
        buttons[2] = findViewById(R.id.btn02);
        buttons[3] = findViewById(R.id.btn10);
        buttons[4] = findViewById(R.id.btn11);
        buttons[5] = findViewById(R.id.btn12);
        buttons[6] = findViewById(R.id.btn20);
        buttons[7] = findViewById(R.id.btn21);
        buttons[8] = findViewById(R.id.btn22);


        for (Button button : buttons) {
            button.setBackgroundColor(buttonColor);
        }

        statusText = findViewById(R.id.statusText);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int id = getResources().getIdentifier("btn" + i + j, "id", getPackageName());
                buttons[i * 3 + j] = findViewById(id);
                buttons[i * 3 + j].setOnClickListener(this);
            }
        }

        // Инициализируем доску пустыми клетками
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    private int getColor(String colorName) {
        switch (colorName) {
            case "Red":
                return Color.rgb(212, 79, 79);
            case "Blue":
                return Color.rgb(79, 79, 212);
            case "Green":
                return Color.rgb(79, 212, 79);

            default:
                return Color.RED;
        }
    }
    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        if (!clickedButton.getText().toString().isEmpty()) {
            return; // Если кнопка не пустая, ничего не делаем
        }

        int clickedId = v.getId();
        int i = (clickedId - R.id.btn00) / 3;
        int j = (clickedId - R.id.btn00) % 3;

        if (isXTurn) {
            clickedButton.setText("X");
            board[i][j] = X;
        } else {
            clickedButton.setText("O");
            board[i][j] = O;
        }

        // Обновить статус игры
        isXTurn = !isXTurn;
        String turnText = (isXTurn) ? "Ходит X" : "Ходит O";
        statusText.setText(turnText);

        // Проверка победы или ничьи
        int winner = checkWinner();
        if (winner != EMPTY) {
            setGameOver((winner == X) ? "X победил!" : "O победил!");
            delayBeforeRestart();
        } else if (isBoardFull()) {
            setGameOver("Ничья!");
            delayBeforeRestart();
        }
    }


    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private int checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][i] != EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[2][0] != EMPTY && board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
            return board[2][0];
        }

        // Если никто не победил, возвращаем EMPTY
        return EMPTY;
    }

    private void setGameOver(String message) {
        statusText.setText(message);

        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }

    public void resetGame() {
        // Очищаем поле
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
                buttons[i * 3 + j].setText("");
            }
        }

        isXTurn = true;
        statusText.setText("Ходит X");

        for (Button button : buttons) {
            button.setEnabled(true);
        }
    }
    private void delayBeforeRestart() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetGame();
            }
        }, 2000);
    }

}