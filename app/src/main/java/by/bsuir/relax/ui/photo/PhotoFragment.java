package by.bsuir.relax.ui.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.dao.Photo;
import by.bsuir.relax.databinding.FragmantPhotoBinding;


public class PhotoFragment extends Fragment {
    private FragmantPhotoBinding binding;
    private Photo photo;

    public PhotoFragment(Photo photo){
        super();
        this.photo=photo;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmantPhotoBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        binding.photo.setImageBitmap(photo.getPhoto());
        binding.deletePhoto.setOnClickListener(view -> {
            DAOFactory.getDAO().removeUserPhoto(photo);
            getParentFragmentManager().popBackStack();
        });
        binding.closePhoto.setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });
        return root;
    }
}
