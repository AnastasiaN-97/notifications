package ru.intersvyaz.course.android.retrofit.ui.userdetails

import android.R.attr
import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.intersvyaz.course.android.retrofit.R
import ru.intersvyaz.course.android.retrofit.data.model.User
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.util.Log
import com.bumptech.glide.request.target.ViewTarget

import ru.intersvyaz.course.android.retrofit.MainActivity
import ru.intersvyaz.course.android.retrofit.ui.main.MainFragment
import java.nio.charset.Charset
import android.R.attr.src
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import android.R.attr.src
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class UserDetailsFragment : Fragment(R.layout.user_fragment) {

    private  val CHANNEL_ID = "channel_id_1"
    private val notificationId = 1


    private val userId by lazy { arguments?.getString(USER_ID) }
    private val viewModel: UserDetailsViewModel by lazy { ViewModelProvider(this).get(UserDetailsViewModel::class.java) }

    private val userObserver = Observer<User?> {
        it ?: return@Observer
        requireView().findViewById<TextView>(R.id.userName).text = it.name
        requireView().findViewById<TextView>(R.id.userId).text = it.id
        requireView().findViewById<TextView>(R.id.userEmail).text = it.email

        val name = requireView().findViewById<TextView>(R.id.userName).text.toString()
        val email = requireView().findViewById<TextView>(R.id.userEmail).text.toString()
        val id = requireView().findViewById<TextView>(R.id.userId).text.toString()
        val imageViewAvatar = requireView().findViewById<ImageView>(R.id.userAvatar)
        val img = it.avatar

        val shareBtn = requireView().findViewById<ImageButton>(R.id.shareBtn)
        val notificationBtn = requireView().findViewById<ImageButton>(R.id.notificationBtn)

        Glide.with(imageViewAvatar.context)
            .load(it.avatar)
            .into(imageViewAvatar)

        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Имя: $name;\nПочта: $email")
            startActivity(Intent.createChooser(intent, "Share"))
        }


        notificationBtn.setOnClickListener {
            sendNotification(name, email, img, id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //передаем из БД
        userId?.let { viewModel.loadUserInfoDB(it) }
        viewModel.user.observe(viewLifecycleOwner, userObserver)
    }

    companion object {
        const val TAG = "userFragment"
        const val USER_ID = "userId"
    }


    private fun sendNotification(name: String, email: String, img: String, id: String) {

        val intent = Intent(requireContext(), UserDetailsFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)
        //val bitmapMy = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher_background)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID )
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Пользователь: $id")
            .setContentText("$name, $email ")
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
           // .setLargeIcon(bitmapMy)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(requireContext())){
            notify(notificationId,builder.build())
        }
    }
}