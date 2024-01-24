package dragosholban.com.androidpuzzlegame

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class Open {
    fun openPicture(pickMedia : ActivityResultLauncher<PickVisualMediaRequest>){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}