package com.example.cacadordebugs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cacadordebugs.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final ArrayList<Bug> listaBugs = new ArrayList<>();

    private BugAdapter adapter;

    private int proximoCodigo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarRecyclerView();
        configurarGravidade();
        configurarCadastro();

        atualizarPainel();
        atualizarMensagemListaVazia();
    }

    // =========================================================
    // RECYCLERVIEW
    // =========================================================

    private void configurarRecyclerView() {

        adapter = new BugAdapter(
                listaBugs,
                this::alternarStatusBug,
                this::removerBug
        );

        binding.recyclerBugs.setLayoutManager(
                new LinearLayoutManager(this)
        );

        binding.recyclerBugs.setAdapter(adapter);
    }

    // =========================================================
    // SEEKBAR DE GRAVIDADE
    // =========================================================

    private void configurarGravidade() {

        atualizarTextoGravidade();

        binding.seekBarGravidade.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {
                        atualizarTextoGravidade();
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {
                    }
                }
        );
    }

    private void atualizarTextoGravidade() {

        int gravidade = obterGravidadeSelecionada();

        String classificacao = obterClassificacaoGravidade(
                gravidade
        );

        binding.tvGravidade.setText(
                getString(
                        R.string.gravidade_atual,
                        gravidade,
                        classificacao
                )
        );

        binding.tvGravidade.setTextColor(
                obterCorGravidade(gravidade)
        );
    }

    private int obterGravidadeSelecionada() {

        return binding.seekBarGravidade.getProgress() + 1;
    }

    private String obterClassificacaoGravidade(int gravidade) {

        switch (gravidade) {

            case 1:
            case 2:
                return getString(R.string.gravidade_baixa);

            case 3:
                return getString(R.string.gravidade_media);

            case 4:
                return getString(R.string.gravidade_alta);

            case 5:
                return getString(R.string.gravidade_critica);

            default:
                return "";
        }
    }

    private int obterCorGravidade(int gravidade) {

        switch (gravidade) {

            case 1:
            case 2:
                return getColor(R.color.bug_low);

            case 3:
                return getColor(R.color.bug_medium);

            case 4:
                return getColor(R.color.bug_high);

            case 5:
                return getColor(R.color.bug_critical);

            default:
                return Color.WHITE;
        }
    }

    // =========================================================
    // CADASTRO
    // =========================================================

    private void configurarCadastro() {

        binding.btnRegistrarBug.setOnClickListener(
                v -> cadastrarBug()
        );
    }

    private void cadastrarBug() {

        String titulo = binding.etTituloBug
                .getText()
                .toString()
                .trim();

        String modulo = binding.etModuloBug
                .getText()
                .toString()
                .trim();

        String descricao = binding.etDescricaoBug
                .getText()
                .toString()
                .trim();

        // Limpa erros anteriores
        binding.etTituloBug.setError(null);
        binding.etModuloBug.setError(null);
        binding.etDescricaoBug.setError(null);

        // Validação do título
        if (titulo.isEmpty()) {

            binding.etTituloBug.setError(
                    getString(R.string.erro_titulo)
            );

            binding.etTituloBug.requestFocus();

            return;
        }

        // Validação do módulo
        if (modulo.isEmpty()) {

            binding.etModuloBug.setError(
                    getString(R.string.erro_modulo)
            );

            binding.etModuloBug.requestFocus();

            return;
        }

        // Validação da descrição
        if (descricao.length() < 10) {

            binding.etDescricaoBug.setError(
                    getString(R.string.erro_descricao)
            );

            binding.etDescricaoBug.requestFocus();

            return;
        }

        int gravidade = obterGravidadeSelecionada();

        String codigo = String.format(
                "BUG-%03d",
                proximoCodigo
        );

        Bug novoBug = new Bug(
                codigo,
                titulo,
                modulo,
                descricao,
                gravidade,
                false,
                R.drawable.ic_bug
        );

        listaBugs.add(novoBug);

        int novaPosicao = listaBugs.size() - 1;

        adapter.notifyItemInserted(novaPosicao);

        binding.recyclerBugs.scrollToPosition(
                novaPosicao
        );

        proximoCodigo++;

        limparFormulario();

        atualizarPainel();
        atualizarMensagemListaVazia();

        Toast.makeText(
                this,
                getString(R.string.bug_registrado, codigo),
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // LIMPAR FORMULÁRIO
    // =========================================================

    private void limparFormulario() {

        binding.etTituloBug.setText("");
        binding.etModuloBug.setText("");
        binding.etDescricaoBug.setText("");

        binding.etTituloBug.setError(null);
        binding.etModuloBug.setError(null);
        binding.etDescricaoBug.setError(null);

        binding.seekBarGravidade.setProgress(0);

        atualizarTextoGravidade();
    }

    // =========================================================
    // CLIQUE NO BUG
    // =========================================================

    private void alternarStatusBug(Bug bug) {

        bug.setCorrigido(
                !bug.isCorrigido()
        );

        int posicao = listaBugs.indexOf(bug);

        if (posicao != -1) {

            adapter.notifyItemChanged(posicao);
        }

        atualizarPainel();
    }

    // =========================================================
    // CLIQUE LONGO
    // =========================================================

    private void removerBug(Bug bug) {

        int posicao = listaBugs.indexOf(bug);

        if (posicao == -1) {
            return;
        }

        listaBugs.remove(posicao);

        adapter.notifyItemRemoved(posicao);

        atualizarPainel();
        atualizarMensagemListaVazia();

        Toast.makeText(
                this,
                getString(
                        R.string.bug_removido,
                        bug.getCodigo()
                ),
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // PAINEL
    // =========================================================

    private void atualizarPainel() {

        int total = listaBugs.size();

        int abertos = 0;
        int corrigidos = 0;
        int pontuacao = 0;

        for (Bug bug : listaBugs) {

            if (bug.isCorrigido()) {

                corrigidos++;

                pontuacao += bug.getGravidade() * 10;

            } else {

                abertos++;
            }
        }

        binding.tvValorTotal.setText(
                String.valueOf(total)
        );

        binding.tvValorAbertos.setText(
                String.valueOf(abertos)
        );

        binding.tvValorCorrigidos.setText(
                String.valueOf(corrigidos)
        );

        binding.tvPontuacao.setText(
                getString(
                        R.string.painel_pontuacao_valor,
                        pontuacao
                )
        );
    }

    // =========================================================
    // LISTA VAZIA
    // =========================================================

    private void atualizarMensagemListaVazia() {

        if (listaBugs.isEmpty()) {

            binding.tvListaVazia.setVisibility(
                    View.VISIBLE
            );

            binding.recyclerBugs.setVisibility(
                    View.GONE
            );

        } else {

            binding.tvListaVazia.setVisibility(
                    View.GONE
            );

            binding.recyclerBugs.setVisibility(
                    View.VISIBLE
            );
        }
    }
}