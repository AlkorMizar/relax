package by.bsuir.relax;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.databinding.RegisterActivityBinding;
import by.bsuir.relax.ui.intro.IntroFragment;

public class RegistrActivity extends AppCompatActivity {

    private RegisterActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DAOFactory.getDAO();
        replaceFragment(new IntroFragment());

    }

    public void replaceFragment(Fragment fragment){
        if(fragment!=null){
            FragmentTransaction trans = this.getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_container,fragment).addToBackStack( "tag" ).commit();
        }

    }
}
