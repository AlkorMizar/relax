package by.bsuir.relax.ui.signin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.Calendar;

import by.bsuir.relax.MainActivity;
import by.bsuir.relax.RegistrActivity;
import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.user.User;
import by.bsuir.relax.user.UserDistributor;
import by.bsuir.relax.databinding.FragmentSingInBinding;
import by.bsuir.relax.util.DrawableUtil;

public class SignInFragment extends Fragment {
    private FragmentSingInBinding binding;
    RegistrActivity activity;
    private DatePickerDialog datePickerDialog;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getData()!=null) {
                    Uri path = result.getData().getData();
                    binding.avatarInp.setImageURI(path);
                }
            });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSingInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = (RegistrActivity) this.getActivity();

        binding.signInBtn.setOnClickListener(view -> {
            try {
                User user=generateUser();
                DAOFactory.getDAO().register(user);

                UserDistributor.login(user);
                Intent intent = new Intent(container.getContext(), MainActivity.class);
                startActivity(intent);
            } catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        binding.editProfBtn.setOnClickListener(view -> {
            if(binding.profileLt.getVisibility()==View.VISIBLE){
                binding.profileLt.setVisibility(View.INVISIBLE);
            }else {
                binding.profileLt.setVisibility(View.VISIBLE);
            }
        });

        binding.avatarInp.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        initDatePicker();
        binding.ageInp.setOnClickListener(view -> {
            datePickerDialog.show();
        });

        return root;
    }

    private User generateUser() throws Exception {
        User user=new User();
        user.setEmail(binding.emailInp.getText().toString());
        user.setPassword(binding.passInp.getText().toString());
        user.setName(binding.nameInp.getText().toString());
        user.setBirthday(binding.ageInp.getText().toString());
        user.setPressure(binding.pressureInp.getText().toString());
        user.setWeight(binding.weightInp.getText().toString());
        user.setHeight(binding.heightInp.getText().toString());
        user.setNumber(binding.numberInp.getText().toString());
        user.setAvatar(DrawableUtil.drawableToBitmap(binding.avatarInp.getDrawable()));
        return user;
    }



    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            binding.ageInp.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this.getContext(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return LocalDate.of(year,month,day).toString();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}