/**
 * HiLo is a guessing game
 *
 * @author Marjan Tropper (trop0008@algonquinlive.com)
 */

package ca.edumedia.trop0008.hilo;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends Activity {


    private static final String ABOUT_DIALOG_TAG = "About Dialog";

    final int MAX_Num_GUESSES = 10;

    private int theNumber;
    private int remainingGuesses;

    private TextView NumGuesses;
    private Button btnGuess;
    private EditText txtUserGuess;
    private ImageView iconImge;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiate global variables
        NumGuesses = (TextView) findViewById(R.id.NumGuesses);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        txtUserGuess = (EditText) findViewById(R.id.txtUserGuess);
        iconImge = (ImageView) findViewById(R.id.iconImge);

        //set longClick function to reset button
        findViewById(R.id.btnReset).setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                onLongClickListener();
                return true;
            }
        });

        //call a new game
        newGame();
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            DialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newGame() {
        //new random Game
        Random randomNumGenerator = new Random();
        theNumber = randomNumGenerator.nextInt(1000) + 1;

        Log.i("TheNumber", Integer.toString(theNumber));

        //default values
        remainingGuesses = MAX_Num_GUESSES;
        NumGuesses.setText(getResources().getString(R.string.game_guesses_remaining, remainingGuesses));
        btnGuess.setText(R.string.btn_guess);
        iconImge.setImageResource(R.drawable.happyicon);
    }

    private void checkNumber() {

        //Message to user
        String msg = "";
        //decrease number of remaining guesses
        remainingGuesses--;


        //image id to show in iconImge
        int imageId = R.drawable.happyicon;
        //check to see the number of remaining guesses
        if (remainingGuesses >= 0) {
            //make sure the user has entered a number
            try {
                //get users input
                int guessNumber = 0;
                guessNumber = Integer.parseInt(txtUserGuess.getText().toString());

                //check user input
                if (guessNumber == theNumber) {

                    //check win message
                    if ((MAX_Num_GUESSES - remainingGuesses) <= 5) {
                        msg = getResources().getString(R.string.game_superior_win);
                    } else {
                        msg = getResources().getString(R.string.game_excellent_win);
                    }

                    //win image
                    imageId = R.drawable.winnericon;
                    NumGuesses.setText(msg);
                    remainingGuesses = 0;

                } else {
                    NumGuesses.setText(getResources().getString(R.string.game_guesses_remaining, remainingGuesses));
                    if (remainingGuesses == 0) {

                        //lost the game
                        msg = getResources().getString(R.string.game_lost);
                        //update image to loser image
                        imageId = R.drawable.losericon;

                    } else if (guessNumber > theNumber) {

                        //too high
                        msg = getResources().getString(R.string.game_high_guess, guessNumber);


                    } else if (guessNumber < theNumber) {

                        //too low
                        msg = getResources().getString(R.string.game_low_guess, guessNumber);

                    }


                }

            } catch (NumberFormatException nfe) {
                NumGuesses.setText(getResources().getString(R.string.game_guesses_remaining, remainingGuesses));
                msg = getResources().getString(R.string.game_not_number);
                    if (remainingGuesses == 0){   imageId = R.drawable.losericon;}
            }
        } else {

            msg = getResources().getString(R.string.game_Reset);
            imageId = R.drawable.losericon;
            btnGuess.setText(R.string.game_Reset);
            NumGuesses.setText(getResources().getString(R.string.game_guesses_remaining, remainingGuesses));
            // if user coninues to press the button after being prompted to reset the button, automatically reset the game
            if (remainingGuesses < 0) {
                msg = "Game Reset!";
                showToast(msg);
                newGame();
                imageId = R.drawable.happyicon;

            }

        }
        //update screen
        txtUserGuess.setText("");
        iconImge.setImageResource(imageId);

        if (remainingGuesses == 0) {
            btnGuess.setText(R.string.game_Reset);
        }

        // provide user with feedback
        showToast(msg);
    }

    private void onLongClickListener() {

        //show message
        showToast(getResources().getString(R.string.game_random_number, theNumber));
        //reset game
        newGame();
    }

    public void onClickBtnReset(View v) {
        newGame();
    }

    public void onClickBtnGuess(View v) {
        checkNumber();
    }

    public void showToast(String text) {
        //check to see if there is a toast already visible or not and than display else reset toast message to new message
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.cancel();
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

}
