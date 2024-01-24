package dragosholban.com.androidpuzzlegame;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.pumpkin.game.quiz.R;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        AssetManager am = getAssets();
        try {
            final String[] files = am.list("img");

            GridView grid = findViewById(R.id.grid_g);
            grid.setAdapter(new ImageAdapter(this));
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                    intent.putExtra("assetName", files[i % files.length]);
                    startActivity(intent);
                }
            });

            // Registers a photo picker activity launcher in single-select mode.
            pickMedia =
                    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                        // Callback is invoked after the user selects a media item or closes the
                        // photo picker.
                        if (uri != null) {
                            Intent intent = new Intent(this, PuzzleActivity.class);
                            intent.putExtra("mCurrentPhotoUri", uri.toString());
                            startActivity(intent);
                        } else {
//                            Log.d("PhotoPicker", "No media selected");
                        }
                    });
        } catch (Exception ignored) {
        }
    }

    public void onImageFromGalleryClick(View view) {
        if (pickMedia != null) {
            new Open().openPicture(pickMedia);
        }

    }
}
