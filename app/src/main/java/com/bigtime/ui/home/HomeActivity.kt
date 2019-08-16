package com.bigtime.ui.home


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bigtime.R
import com.bigtime.binding.BindingAdapters
import com.bigtime.ui.BaseActivity
import com.bigtime.ui.add_product.AddProductActivity
import com.bigtime.ui.approved_product.ApprovedProductActivity
import com.bigtime.ui.login.LoginActivity
import com.bigtime.ui.order.OrderActivity
import com.bigtime.ui.pending_product.PendingProductActivity
import com.bigtime.utils.SessionUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.material.navigation.NavigationView
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor


class HomeActivity : BaseActivity() ,View.OnClickListener{



    lateinit var toolbar: Toolbar

    lateinit var drawerLayout: DrawerLayout

    //lateinit var mdrawerToggle: ActionBarDrawerToggle

    lateinit var navController: NavController

    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        /* if (!SessionUtils.hasSession() || !SessionUtils.isLoggedIn!!) {
             startActivity(intentFor<LoginActivity>())
             super.onCreate(savedInstanceState)
             finishAffinity()
             return
         }*/

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        setupNavigation()

    }


    override fun onSupportNavigateUp(): Boolean {
        lastNavigationItemSelected = R.id.home
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    var lastNavigationItemSelected = R.id.home

    /*override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.setChecked(true)

        drawerLayout.closeDrawers()

        val id = menuItem.getItemId()

        if (lastNavigationItemSelected == id) return true


        if (lastNavigationItemSelected != R.id.home) {
            navController.popBackStack()
        }


        lastNavigationItemSelected = id


        return true
    }*/

    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.tv_order -> {
                startActivity(intentFor<OrderActivity>())
            }
            R.id.tv_add -> {
                startActivity(intentFor<AddProductActivity>())
            }

            R.id.tv_approved -> {

                startActivity(intentFor<ApprovedProductActivity>())
            }
            R.id.tv_pending -> {
                togglePendingVisibility()
                return
            }

            R.id.tv_draft -> {
                startActivity(intentFor<PendingProductActivity>())
            }

            R.id.tv_review -> {
                startActivity(intentFor<PendingProductActivity>())
            }

            R.id.tv_rejected-> {
                startActivity(intentFor<PendingProductActivity>())
            }

            R.id.tv_logout -> {
                logout()
            }
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    var rotationAngle:Float =0F
    private fun togglePendingVisibility() {

        var visibile =lPending.visibility == View.VISIBLE

        //rotationAngle =if(rotationAngle == 0f )  180f else 0f;  //toggle



        if (visibile){
            lPending.visibility = View.GONE
            rotationAngle = 180f

        }else{
            lPending.visibility = View.VISIBLE

            rotationAngle = 0f
        }


        ivArrow.animate().rotation(rotationAngle).setDuration(500).start();

    }

    fun logout() {
        val alert = AlertDialog.Builder(this!!)
        alert.setMessage("Are you sure?")
        alert.setPositiveButton(R.string.yes) { dialog, which ->
            //clearCredentials();
            //mViewModel.logOut()
        }
        alert.setNegativeButton(R.string.no, { dialog, which -> dialog.dismiss() })
        alert.show()

    }

    lateinit var lPending:RelativeLayout
    lateinit var ivArrow:ImageView
    // Setting Up One Time Navigation
    private fun setupNavigation() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)


        navigationView = findViewById(R.id.nav_view)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        NavigationUI.setupWithNavController(navigationView, navController)

        //navigationView.setNavigationItemSelectedListener(this)

        supportActionBar!!.setTitle(R.string.app_name)
        //toolbar.setTitle(R.string.app_name)
        /*var session= SessionUtils.getLoginSession()


        navigationView.getHeaderView(0).findViewById<TextView>(R.id.txt_name).setText(session.name())

        BindingAdapters.request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(session.getImage()))
                .setResizeOptions(ResizeOptions(60, 60))
                .build()

        var  imageView = navigationView.getHeaderView(0).findViewById<SimpleDraweeView>(R.id.img_user)
        imageView.controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.controller)
                .setImageRequest(BindingAdapters.request)
                .build()*/


        navigationView.findViewById<View>(R.id.tv_order).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_add).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_approved).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_pending).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_logout).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_draft).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_review).setOnClickListener(this)
        navigationView.findViewById<View>(R.id.tv_rejected).setOnClickListener(this)
        lPending=navigationView.findViewById(R.id.l_pending)
        ivArrow=navigationView.findViewById(R.id.iv_arrow)

    }


}
