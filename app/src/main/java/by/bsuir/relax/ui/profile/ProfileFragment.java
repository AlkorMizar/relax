package by.bsuir.relax.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import by.bsuir.relax.R;
import by.bsuir.relax.RelaxApplication;
import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.dao.Photo;
import by.bsuir.relax.databinding.FragmentProfileBinding;
import by.bsuir.relax.ui.photo.PhotoFragment;
import by.bsuir.relax.user.User;
import by.bsuir.relax.user.UserDistributor;
import by.bsuir.relax.util.DrawableUtil;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    LayoutInflater inflater;
    private DatePickerDialog datePickerDialog;
    private final ActivityResultLauncher<Intent> avaChangeResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getData()!=null) {
                    Uri path = result.getData().getData();
                    binding.avatarInp.setImageURI(path);
                }
            });

    private final ActivityResultLauncher<Intent> addPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getData()!=null) {
                    Uri path = result.getData().getData();
                    Drawable drawable=null;
                    try {
                        InputStream inputStream = RelaxApplication.getAppContext().getContentResolver().openInputStream(path);
                        drawable = Drawable.createFromStream(inputStream, path.toString() );
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getContext(),"Could not load image",Toast.LENGTH_LONG).show();
                        return;
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    ConstraintLayout imgAndText = (ConstraintLayout) inflater.inflate(R.layout.phot_fr,binding.gridLayout,false);

                    ImageView img=imgAndText.findViewById(R.id.imageSample);
                    img.setImageDrawable(drawable);

                    TextView txt=imgAndText.findViewById(R.id.date);
                    Calendar calendar=GregorianCalendar.getInstance();
                    @SuppressLint("DefaultLocale") String date=String.format("%d:%d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
                    txt.setText(date);
                    binding.gridLayout.addView(imgAndText, 0);

                    DAOFactory.getDAO().addUsersPhotos(UserDistributor.getUser(), DrawableUtil.drawableToBitmap(drawable),date);
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.inflater=inflater;

        setPhotos();

        binding.editProfBtn2.setOnClickListener(view -> {
            if(binding.profileLt2.getVisibility()==View.VISIBLE){
                binding.profileLt2.setVisibility(View.INVISIBLE);
                binding.gridLayout.setVisibility(View.VISIBLE);
            }else {
                binding.profileLt2.setVisibility(View.VISIBLE);
                binding.gridLayout.setVisibility(View.INVISIBLE);
            }
        });

        binding.editUser.setOnClickListener(view -> {
            try {
                User user=editUser();
                DAOFactory.getDAO().updateUser(user);
                UserDistributor.login(user);
                binding.userName.setText(UserDistributor.getUser().getName());
                binding.profileLt2.setVisibility(View.INVISIBLE);
                binding.gridLayout.setVisibility(View.VISIBLE);
                binding.profileImage.setImageBitmap(UserDistributor.getUser().getAvatar());
                UserDistributor.getUser().setAvatar(UserDistributor.getUser().getAvatar());
            } catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        binding.avatarInp.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            avaChangeResultLauncher.launch(intent);
        });

        binding.addPhotoBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            addPhotoResultLauncher.launch(intent);
        });

        initDatePicker();
        binding.ageInp.setOnClickListener(view -> {
            datePickerDialog.show();
        });

        binding.userName.setText(UserDistributor.getUser().getName());

        binding.profileImage.setImageBitmap(UserDistributor.getUser().getAvatar());

        setUserProf();
        onBackPressed();

        return root;
    }

    private void onBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                setPhotos();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

    }

    private void setPhotos(){
        List<Photo> photos= DAOFactory.getDAO().usersPhotos(UserDistributor.getUser());

        ConstraintLayout imgAndText;
        for (Photo photo: photos) {
            imgAndText = (ConstraintLayout) inflater.inflate(R.layout.phot_fr,binding.gridLayout,false);
            ImageView img=imgAndText.findViewById(R.id.imageSample);
            img.setImageBitmap(photo.getPhoto());
            TextView txt=imgAndText.findViewById(R.id.date);
            txt.setText(photo.getDate());
            binding.gridLayout.addView(imgAndText, 0);
            imgAndText.setOnClickListener(view -> {
                FragmentTransaction trans = getParentFragmentManager().beginTransaction();;
                trans.replace(R.id.nav_host_fragment_content_main,new PhotoFragment(photo)).addToBackStack( "tag" ).commit();
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void setUserProf(){
        User user= UserDistributor.getUser();
        binding.nameInp.setText(user.getName());
        binding.ageInp.setText(user.getBirthday().toString());
        binding.pressureInp.setText(user.getPressure().first+"/"+user.getPressure().second);
        binding.weightInp.setText(user.getWeight()+"");
        binding.heightInp.setText(user.getHeight()+"");
        binding.numberInp.setText(user.getNumber());
        binding.avatarInp.setImageBitmap(user.getAvatar());
    }

    private User editUser() throws Exception {
        User user= UserDistributor.getUser().clone();
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
