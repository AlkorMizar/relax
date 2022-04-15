package by.bsuir.relax.ui.statistic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import by.bsuir.relax.R;
import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.databinding.FragmentStatisticBinding;
import by.bsuir.relax.ui.recommended.RecommendedFragment;
import by.bsuir.relax.user.UserDistributor;
import kotlin.Pair;

public class StatisticFragment extends Fragment {
    FragmentStatisticBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final StatisticAdapter adapter = new StatisticAdapter(this.getContext(),this.getActivity(), DAOFactory.getDAO().getUsersMoods(UserDistributor.getUser()));
        ListView listView=binding.statisticShit;
        listView.setAdapter(adapter);
        binding.recommendFromStat.setOnClickListener(view -> {
            FragmentTransaction trans = getParentFragmentManager().beginTransaction();;
            trans.replace(R.id.nav_host_fragment_content_main,new RecommendedFragment()).commit();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
