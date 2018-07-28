package ml.shahidkamal.popularmovies.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.Models.Trailer;
import ml.shahidkamal.popularmovies.R;

/**
 * Created by shaah on 26-07-2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.mViewHolder> {

    ArrayList<Trailer> trailers = new ArrayList<>();
    Context context;

    public TrailerAdapter(Context context, ArrayList< Trailer> trailers){
        this.context = context;
        this.trailers.addAll(trailers);
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailers_row, null, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        final Trailer trailer = trailers.get(position);
        holder.trailerName.setText(trailer.name);
        holder.trailerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYoutubeOrBrowser(trailer);
            }
        });
    }

    private void openYoutubeOrBrowser(Trailer trailer) {
        Toast.makeText(context, "Opening " + trailer.name, Toast.LENGTH_LONG).show();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.youtubeId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.youtubeId));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent); // open view intent
        }
    }

    @Override
    public int getItemCount() {
        return this.trailers.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{

        public TextView trailerName;

        public mViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.tvTrailerName);
        }
    }
}
