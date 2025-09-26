import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame {
    JTextField addField = new JTextField();
    JButton button = new JButton();
    JButton button2 = new JButton();
    JButton button3 = new JButton();
    JButton button4 = new JButton();

    String[] colunas = {"Tarefa", "Concluido"};
    DefaultTableModel model = new DefaultTableModel(colunas, 0);
    JTable table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);

    private List<Tarefa> tarefas;
    private Gson gson = new Gson();
    private File arquivoTarefas = new File("tarefas.json");

    public App() {
        super();
        this.tarefas = carregarTarefas();
        initialize();
    }

    public void initialize() {
        setTitle("ToDo");
        setSize(600, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Tabela
        scrollPane.setBounds(35, 20, 505, 500);
        add(scrollPane);

        // Adicionar itens
        addField.setBounds(35, 550, 250, 30);
        button.setBounds(295, 550, 80, 30);
        button.setText("Inserir");
        button2.setBounds(380, 550, 80, 30);
        button2.setText("Alterar");
        button3.setBounds(465, 550, 80, 30);
        button3.setText("Excluir");
        button4.setBounds(35, 590, 510, 30);
        button4.setText("Marcar como Concluido");
        add(addField);
        add(button);
        add(button2);
        add(button3);
        add(button4);

        // Preencher a tabela com os dados do arquivo
        for (Tarefa t : tarefas) {
            model.addRow(new Object[]{t.getNome(), t.getStatus()});
        }
        
        // Lógica dos botões
        button.addActionListener(e -> {
            String nome = addField.getText();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Digite um nome!");
                return;
            } else {
                Tarefa novaTarefa = new Tarefa(nome);
                tarefas.add(novaTarefa);
                model.addRow(new Object[]{novaTarefa.getNome(), novaTarefa.getStatus()});
                addField.setText("");
                salvarTarefas();
            }
        });

        button4.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tarefas.get(selectedRow).setStatus("Sim");
                model.setValueAt("Sim", selectedRow, 1);
                salvarTarefas();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para marcar como concluída.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        button2.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String novoNome = JOptionPane.showInputDialog(this, "Digite o novo nome para a tarefa:");
                if (novoNome != null && !novoNome.trim().isEmpty()) {
                    tarefas.get(selectedRow).setNome(novoNome);
                    model.setValueAt(novoNome, selectedRow, 0);
                    salvarTarefas();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        button3.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tarefas.remove(selectedRow);
                model.removeRow(selectedRow);
                salvarTarefas();
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void salvarTarefas() {
        try (FileWriter writer = new FileWriter(arquivoTarefas)) {
            gson.toJson(tarefas, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar as tarefas: " + e.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private List<Tarefa> carregarTarefas() {
        if (!arquivoTarefas.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(arquivoTarefas)) {
            java.lang.reflect.Type tipoListaTarefa = new TypeToken<List<Tarefa>>() {}.getType();
            List<Tarefa> tarefasCarregadas = gson.fromJson(reader, tipoListaTarefa);
            return tarefasCarregadas != null ? tarefasCarregadas : new ArrayList<>();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar as tarefas: " + e.getMessage(), "Erro de Carregamento", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}