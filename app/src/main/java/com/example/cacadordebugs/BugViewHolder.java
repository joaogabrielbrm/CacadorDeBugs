package com.example.cacadordebugs;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cacadordebugs.databinding.ItemBugBinding;

public class BugViewHolder extends RecyclerView.ViewHolder {

    private final ItemBugBinding binding;

    public BugViewHolder(@NonNull ItemBugBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Bug bug) {

        // Dados do bug
        binding.tvCodigoBug.setText(bug.getCodigo());
        binding.tvTituloBug.setText(bug.getTitulo());
        binding.tvModuloBug.setText(bug.getModulo());
        binding.tvDescricaoBug.setText(bug.getDescricao());

        // Imagem
        binding.ivBug.setImageResource(
                bug.getImagemId()
        );

        // Gravidade
        String textoGravidade = obterTextoGravidade(
                bug.getGravidade()
        );

        binding.tvGravidadeBug.setText(
                itemView.getContext().getString(
                        R.string.texto_gravidade,
                        itemView.getContext().getString(
                                R.string.label_gravidade_item
                        ),
                        bug.getGravidade(),
                        textoGravidade
                )
        );

        binding.tvGravidadeBug.setTextColor(
                obterCorGravidade(bug.getGravidade())
        );

        // Status e aparência
        if (bug.isCorrigido()) {

            binding.tvStatusBug.setText(
                    R.string.status_corrigido
            );

            binding.tvStatusBug.setTextColor(
                    itemView.getContext().getColor(
                            R.color.bug_fixed
                    )
            );

            // Bug corrigido fica visualmente mais discreto
            binding.tvCodigoBug.setAlpha(0.6f);
            binding.tvTituloBug.setAlpha(0.7f);
            binding.tvModuloBug.setAlpha(0.7f);
            binding.tvDescricaoBug.setAlpha(0.7f);
            binding.ivBug.setAlpha(0.5f);

        } else {

            binding.tvStatusBug.setText(
                    R.string.status_aberto
            );

            binding.tvStatusBug.setTextColor(
                    itemView.getContext().getColor(
                            R.color.bug_open
                    )
            );

            // Bug aberto volta à aparência normal
            binding.tvCodigoBug.setAlpha(1.0f);
            binding.tvTituloBug.setAlpha(1.0f);
            binding.tvModuloBug.setAlpha(1.0f);
            binding.tvDescricaoBug.setAlpha(1.0f);
            binding.ivBug.setAlpha(1.0f);
        }
    }

    private String obterTextoGravidade(int gravidade) {
        switch (gravidade) {
            case 1:
            case 2:
                return itemView.getContext().getString(R.string.gravidade_baixa);

            case 3:
                return itemView.getContext().getString(R.string.gravidade_media);

            case 4:
                return itemView.getContext().getString(R.string.gravidade_alta);

            case 5:
                return itemView.getContext().getString(R.string.gravidade_critica);

            default:
                return "";
        }
    }

    private int obterCorGravidade(int gravidade) {

        switch (gravidade) {

            case 1:
            case 2:
                return itemView.getContext().getColor(
                        R.color.bug_low
                );

            case 3:
                return itemView.getContext().getColor(
                        R.color.bug_medium
                );

            case 4:
                return itemView.getContext().getColor(
                        R.color.bug_high
                );

            case 5:
                return itemView.getContext().getColor(
                        R.color.bug_critical
                );

            default:
                return Color.WHITE;
        }
    }
}