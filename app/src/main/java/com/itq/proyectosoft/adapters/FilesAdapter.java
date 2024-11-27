package com.itq.proyectosoft.adapters;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileViewHolder> {

    private final List<Uri> fileUris;

    public FilesAdapter(List<Uri> fileUris) {
        this.fileUris = fileUris;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        Uri fileUri = fileUris.get(position);
        holder.textView.setText(fileUri.getLastPathSegment());
    }

    @Override
    public int getItemCount() {
        return fileUris.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public FileViewHolder(View view) {
            super(view);
            textView = view.findViewById(android.R.id.text1);
        }
    }
}
