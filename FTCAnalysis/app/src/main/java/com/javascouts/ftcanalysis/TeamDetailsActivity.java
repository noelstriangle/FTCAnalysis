package com.javascouts.ftcanalysis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import org.apache.commons.lang3.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Liam on 12/9/2017.
 */

public class TeamDetailsActivity extends AppCompatActivity {

    private TeamDatabase mDb;
    private TeamDao mDao;

    private LinearLayout llayout;

    private int teamN;

    private Toast toast1;

    private Team toDelete;
    private Team tempTeam;
    private String teamName, teamInfo;
    private boolean canJewel, canGlyphAuto, canCypher, canSafeZone, canCypherEndgame, canUpright;
    private int teamNumber, glyphs, rows, columns, relics, relicZone, autoPoints, telePoints;

    private static final int SELECT_PHOTO = 100;

    private InputStream imageStream;
    private Bitmap image;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstnceState) {

        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_team_details);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

    }

    @Override
    protected void onResume() {

        super.onResume();
        llayout = findViewById(R.id.llayout);

        llayout.setZ(1);

        teamN = getIntent().getIntExtra("TEAM_NUMBER", 0);
        Log.d("TESTING", "Extra:" + String.valueOf(teamN) + " received.");

        init(teamN);

        toast1 = new Toast(this).makeText(this, "Delete Canceled.", Toast.LENGTH_LONG);
        toast1.setText("Delete Canceled.");

        Toolbar myToolbar = findViewById(R.id.toolbarTD);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.rgb(33,81,8));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Team Details");

        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menus, menu);
        MenuItem toHide1 = menu.findItem(R.id.action_export);
        toHide1.setVisible(false);
        MenuItem toHide2 = menu.findItem(R.id.action_deleteall);
        toHide2.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_settings:

                break;

            case R.id.action_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Delete team " + String.valueOf(teamNumber) + " forever?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                toDelete = mDao.getTeam(teamN);
                                mDao.deleteAll(toDelete);
                            }
                        }).start();

                        finish();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        toast1.setText("Delete Cancelled");
                        toast1.show();

                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                break;

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);

                break;

            case R.id.action_edit:

                editTeamDetails(teamN);

                break;


        }

        return true;

    }

    private void init(final int teamNum) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                mDb = Room.databaseBuilder(getApplicationContext(),
                        TeamDatabase.class, "team-database").build();
                mDao = mDb.getTeamDao();
                Log.d("DETAILCREATION","Database instantiated.");

                tempTeam = mDao.getTeam(teamNum);
                Log.d("DETAILCREATIION", "Team:" + String.valueOf(teamNum) + " received.");

                try {
                    teamName = tempTeam.getTeamName();
                    teamNumber = tempTeam.getTeamNumber();
                    Log.d("DETAILCREATION", "Name: " + teamName + ". Number: " + String.valueOf(teamNumber) + ".");
                    canJewel = tempTeam.getJewelb();
                    canCypher = tempTeam.getAutoCypherb();
                    canGlyphAuto = tempTeam.getGlyphAutob();
                    canSafeZone = tempTeam.getSafeZoneb();
                    canCypherEndgame = tempTeam.getEndGameCypherb();
                    canUpright = tempTeam.getUprightb();
                    glyphs = tempTeam.getGlyphBari();
                    rows = tempTeam.getRowBari();
                    columns = tempTeam.getColumnBari();
                    relics = tempTeam.getRelicBari();
                    relicZone = tempTeam.getRelicZoneBari();
                    autoPoints = tempTeam.getAutoPoints();
                    telePoints = tempTeam.getTelePoints();
                    teamInfo = tempTeam.getOtherNotes();
                    image = getImage(tempTeam.getImage());
                } catch(NullPointerException e) {
                    toast1.setText("Team is being updated.");
                    toast1.show();
                    finish();
                }


                Log.d("DETAILCREATION", "Team info all received.");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initUi();
                    }
                });
            }
        }).start();

        Toolbar myToolbar = findViewById(R.id.toolbarTD);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.rgb(33,81,8));

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initUi() {

        int x = 16;

        TextView teamNameText = new TextView(this);
        teamNameText.setText(teamName);
        teamNameText.setTextSize(32);
        teamNameText.setTop(0 * x);
        teamNameText.setTextColor(android.graphics.Color.rgb(33,81,8));
        teamNameText.setGravity(Gravity.CENTER);

        TextView teamNumText = new TextView(this);
        teamNumText.setText(String.valueOf(teamNumber));
        teamNumText.setTextSize(24);
        teamNumText.setTop(1 * x);
        teamNumText.setTextColor(android.graphics.Color.rgb(33,81,8));
        teamNumText.setGravity(Gravity.CENTER);

        TextView autonomous = new TextView(this);
        autonomous.setText("Autonomous:");
        autonomous.setTextSize(16);
        autonomous.setTop(2 * x);
        autonomous.setTextColor(android.graphics.Color.rgb(33,81,8));
        autonomous.setGravity(Gravity.START);

        TextView canJewelText = new TextView(this);
        canJewelText.setText("- " + returnCanOrCant(canJewel) + " knock the correct jewel off.");
        canJewelText.setTextSize(16);
        canJewelText.setTop(3 * x);
        canJewelText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canJewelText.setGravity(Gravity.START);

        TextView canGlyphAutoText = new TextView(this);
        canGlyphAutoText.setText("- " + returnCanOrCant(canGlyphAuto) + " place the glyph in the box.");
        canGlyphAutoText.setTextSize(16);
        canGlyphAutoText.setTop(4 * x);
        canGlyphAutoText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canGlyphAutoText.setGravity(Gravity.START);

        TextView canAutoCypherText = new TextView(this);
        canAutoCypherText.setText("- " + returnCanOrCant(canCypher) + " place the glyph in the correct column.");
        canAutoCypherText.setTextSize(16);
        canAutoCypherText.setTop(5 * x);
        canAutoCypherText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canAutoCypherText.setGravity(Gravity.START);

        TextView canSafeZoneText = new TextView(this);
        canSafeZoneText.setText("- " + returnCanOrCant(canSafeZone) + " park in the safe zone.");
        canSafeZoneText.setTextSize(16);
        canSafeZoneText.setTop(6 * x);
        canSafeZoneText.setTextColor(android.graphics.Color.rgb(33,81,8));
        canSafeZoneText.setGravity(Gravity.START);

        TextView teleop = new TextView(this);
        teleop.setText("Teleop:");
        teleop.setTextSize(16);
        teleop.setTop(7 * x);
        teleop.setTextColor(android.graphics.Color.rgb(33,81,8));
        teleop.setGravity(Gravity.START);

        TextView glyphText = new TextView(this);
        glyphText.setText("- Can score " + String.valueOf(glyphs) + " glyphs.");
        glyphText.setTextSize(16);
        glyphText.setTop(8 * x);
        glyphText.setTextColor(android.graphics.Color.rgb(33,81,8));
        glyphText.setGravity(Gravity.START);

        TextView rowText = new TextView(this);
        rowText.setText("- Can make " + String.valueOf(rows) + " full rows.");
        rowText.setTextSize(16);
        rowText.setTop(9 * x);
        rowText.setTextColor(android.graphics.Color.rgb(33,81,8));
        rowText.setGravity(Gravity.START);

        TextView columnText = new TextView(this);
        columnText.setText("- Can make " + String.valueOf(columns) + " full columns.");
        columnText.setTextSize(16);
        columnText.setTop(10 * x);
        columnText.setTextColor(android.graphics.Color.rgb(33,81,8));
        columnText.setGravity(Gravity.START);

        TextView relicText = new TextView(this);
        relicText.setText("- Can place " + String.valueOf(relics) + " relics.");
        relicText.setTextSize(16);
        relicText.setTop(11 * x);
        relicText.setTextColor(android.graphics.Color.rgb(33,81,8));
        relicText.setGravity(Gravity.START);

        TextView relicZoneText = new TextView(this);
        relicZoneText.setText("- Can place the relics in zone " + String.valueOf(relicZone) + ".");
        relicZoneText.setTextSize(16);
        relicZoneText.setTop(12 * x);
        relicZoneText.setTextColor(android.graphics.Color.rgb(33,81,8));
        relicZoneText.setGravity(Gravity.START);

        TextView uprightText = new TextView(this);
        uprightText.setText("- " + returnCanOrCant(canUpright) + " place the relics upright.");
        uprightText.setTextSize(16);
        uprightText.setTop(13 * x);
        uprightText.setTextColor(android.graphics.Color.rgb(33,81,8));
        uprightText.setGravity(Gravity.START);

        TextView infoText = new TextView(this);
        infoText.setText("Other notes: " + teamInfo);
        infoText.setTextSize(16);
        infoText.setTop(14 * x);
        infoText.setTextColor(android.graphics.Color.rgb(33,81,8));
        infoText.setGravity(Gravity.START);

        final ImageView imageView = new ImageView(this);

        try {
            imageView.setImageBitmap(image);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }

        imageView.setMaxHeight(520);
        imageView.setMaxWidth(520);
        imageView.setLeft(16);
        imageView.setTop(17 * x);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(imageView, image);
            }
        });

        llayout.addView(teamNameText);
        llayout.addView(teamNumText);
        llayout.addView(autonomous);
        llayout.addView(canJewelText);
        llayout.addView(canGlyphAutoText);
        llayout.addView(canAutoCypherText);
        llayout.addView(canSafeZoneText);
        llayout.addView(teleop);
        llayout.addView(glyphText);
        llayout.addView(rowText);
        llayout.addView(columnText);
        llayout.addView(relicText);
        llayout.addView(relicZoneText);
        llayout.addView(uprightText);
        llayout.addView(infoText);
        llayout.addView(imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        image = decodeUri(selectedImage);
                    } catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Team tempTeam = mDao.getTeam(teamN);
                    tempTeam.setImage(getBytes(image));
                    mDao.updateAll(tempTeam);
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        image = null;
                        recreate();
                    }
                });

            }
        }).start();


    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 520;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

    private String returnCanOrCant(boolean b) {

        if(b) {

            return "Can";

        } else {

            return "Can not";

        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        llayout.removeAllViews();

    }

    private void editTeamDetails(int teamId) {

        Intent intent = new Intent(this, EditTeamActivity.class);
        intent.putExtra("TEAM_NUMBER", teamId);
        startActivity(intent);

    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        try {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch(NullPointerException e) {
            Bitmap b = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.no_image);
            return b;
        }
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap i) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = findViewById(
                R.id.expanded_image);
        expandedImageView.setImageBitmap(i);

        expandedImageView.setZ(5);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.llayout)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

}
