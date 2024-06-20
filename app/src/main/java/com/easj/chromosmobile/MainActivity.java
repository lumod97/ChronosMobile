package com.easj.chromosmobile;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;

import com.easj.chromosmobile.Logica.Swal;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.easj.chromosmobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public NavController navController;

    public boolean isPasswordDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer = binding.drawerLayout;
        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_configuracion)
                .setOpenableLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (!ChronosMobile.isPassPassed() && navDestination.getLabel().toString().equals("Configuraciones")) {
                navController1.popBackStack();
                // Evita abrir el diálogo si ya está mostrándose o si la bandeja está en proceso de cierre

                    Swal.settingsPassword(this, (isValid, sweetAlertDialog) -> {
                        if (isValid) {
                            sweetAlertDialog.dismissWithAnimation();
                            drawer.closeDrawer(GravityCompat.START);

                            ChronosMobile.passPassed = true;
                            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                            navController.navigate(R.id.nav_configuracion);
                            // Manualmente realiza la navegación al destino nav_configuracion
                        } else {
                            Swal.warning(this, "PROHIBIDO", "La contraseña no es correcta");
                        }
                    });
            }
        });

        // Excluir el destino nav_configuracion del AppBarConfiguration
        mAppBarConfiguration = new AppBarConfiguration.Builder(mAppBarConfiguration.getTopLevelDestinations())
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupWithNavController(navigationView, navController);

        if((Build.VERSION.SDK_INT) >= Build.VERSION_CODES.P){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        return true;
    }

    public DrawerLayout obtenerDrawer(){
        return drawer;
    }
    public NavigationView obtenerNavigationView() {
        return navigationView;
    }

    public NavController obtenerNavController() { return navController; }
}