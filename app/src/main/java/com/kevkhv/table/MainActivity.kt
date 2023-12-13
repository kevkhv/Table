package com.kevkhv.table

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kevkhv.table.act.EditAdsAct
import com.kevkhv.table.databinding.ActivityMainBinding
import com.kevkhv.table.dialoghelper.DialogConst
import com.kevkhv.table.dialoghelper.DialogHelper


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount: TextView
    private lateinit var binding: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        tvAccount = binding.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_ads) {
            val intent = Intent(this, EditAdsAct::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun init() {
        setSupportActionBar(binding.mainContent.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.mainContent.toolbar, R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_my_ads -> Toast.makeText(this, "Мои обьявления", Toast.LENGTH_SHORT).show()
            R.id.id_car -> Toast.makeText(this, "Авто", Toast.LENGTH_SHORT).show()
            R.id.id_pc -> Toast.makeText(this, "Компьютеры", Toast.LENGTH_SHORT).show()
            R.id.id_smartphone -> Toast.makeText(this, "Смартфоны", Toast.LENGTH_SHORT).show()
            R.id.id_dm -> Toast.makeText(this, "Бытовая техника", Toast.LENGTH_SHORT).show()
            R.id.id_sign_up -> dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            R.id.id_sign_in -> dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            R.id.id_sign_out -> {
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.signOutGoogleAcc()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?) {
        tvAccount.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            user.email
        }
    }
//    fun uiUpdate(user: FirebaseUser?) {
//        if (user == null) {
//            binding.navView.menu.findItem(R.id.id_sign_out).setVisible(false)
//            binding.navView.menu.findItem(R.id.id_sign_in).setVisible(true)
//            binding.navView.menu.findItem(R.id.id_sign_up).setVisible(true)
//            tvAccount.text = resources.getString(R.string.not_reg)
//        } else {
//            binding.navView.menu.findItem(R.id.id_sign_out).isVisible = true
//            binding.navView.menu.findItem(R.id.id_sign_in).isVisible = false
//            binding.navView.menu.findItem(R.id.id_sign_up).isVisible = false
//            tvAccount.text = user.email
//        }
//
//    }
}