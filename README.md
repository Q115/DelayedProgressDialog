# DelayedProgressDialog
<p align="center">
  <a href="https://github.com/Q115/Goalie_Android">
    <img src="https://i.imgur.com/yTv5F2T.gif">
  </a>
<p>

ProgressDialog is deprecated in Android O. This library was created to mimic the ease of use of ProgressDialog and to also add additional functionalities. Ideal for network operations to prevent loading icon from "flickering" the screen. 

## Install
Just copy dialog_progress.xml to your layout directory, and DelayedProgressDialog.java to your source code directory (change the default package name) and you'll be good to go.

## How to use
    DelayedProgressDialog progressDialog = new DelayedProgressDialog();
    progressDialog.show(getSupportFragmentManager(), "tag");
    
    //dismiss or cancel the dialog
    progressDialog.cancel();

ProgressDialog with a loading icon will appear after DELAY_MILLISECOND if it's not cancelled before then. Content will show for SHOW_MIN_MILLISECOND. Change the values to suit your liking.

    private static final int DELAY_MILLISECOND = 450;
    private static final int SHOW_MIN_MILLISECOND = 300;
    
dialog_progress.xml just shows a ProgressBar, you can add other Views to layout if desired.

## License
Apache 2.0
