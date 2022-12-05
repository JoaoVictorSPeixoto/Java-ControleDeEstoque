package src;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Saida extends JPanel implements FocusListener, ActionListener{
    
    private int altura, largura;
    private JPanel secao_Nome;
    private JPanel secao_Button;
    private JPanel secao_Quantidade;
    private JLabel JLabel_nome;
    private JLabel JLabel_quantidade;
    private JComboBox<String> JComboBox_nome;
    private JTextField textField_quantidade;
    private JButton confirma;
    private Controller controller = Controller.istancia();
    private ArrayList<Produto> produtos;

    public Saida(int ALTURA, int LARGURA){

        largura = LARGURA;
        altura = ALTURA;

       

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(largura,altura));



        JComboBox_nome = new JComboBox<>(preencheComboBoxNome());
        textField_quantidade = new JTextField("Quantidade");
        JLabel_nome = new JLabel("Digite o nome do produto no campo abaixo:");
        JLabel_quantidade = new JLabel("Digite a quantidade de produtos a sairem:");
        secao_Nome = new JPanel();
        secao_Quantidade = new JPanel();
        secao_Button = new JPanel();
        confirma = new JButton("Confirmar");

        confirma.setFocusPainted(false);
        textField_quantidade.setColumns(10);

        secao_Nome.setLayout(new FlowLayout(FlowLayout.CENTER));
        secao_Nome.setPreferredSize(new Dimension(largura,30));
        secao_Nome.add(JLabel_nome);

        secao_Quantidade.setLayout(new FlowLayout(FlowLayout.CENTER));
        secao_Quantidade.setPreferredSize(new Dimension(largura,30));
        secao_Quantidade.add(JLabel_quantidade);
       
        secao_Button.setLayout(new FlowLayout(FlowLayout.CENTER));
        secao_Button.setPreferredSize(new Dimension(largura,50));
        secao_Button.add(confirma);

        JComboBox_nome.addFocusListener(this);
        textField_quantidade.addFocusListener(this);
        confirma.addActionListener(this);

        AutoCompleteDecorator.decorate(JComboBox_nome);

        this.add(secao_Nome);
        this.add(JComboBox_nome);
        this.add(secao_Quantidade);
        this.add(textField_quantidade);
        this.add(secao_Button);
        

    }
    public String[] preencheComboBoxNome(){
        produtos = controller.getProdutos();
        String[] nomes = new String[produtos.size()+1];
        nomes[0] = "";
        for (int i = 1; i <= produtos.size(); i++) {
            nomes[i] = produtos.get(i-1).getNome();
        }
        
        return nomes;
    }

    public void setComboBoxNome(){
        JComboBox_nome.removeAllItems();
        String[] nomes = preencheComboBoxNome();
        for(int i = 0; i < nomes.length; i++){
            JComboBox_nome.addItem(nomes[i]);
        }
        repaintAll();
    }

    public void repaintAll(){
        this.repaint();
        JComboBox_nome.repaint();
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource().equals(JComboBox_nome)){
        }else if (e.getSource().equals(textField_quantidade)){
            textField_quantidade.setText("");
        }
       
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(textField_quantidade.getText().equals("")){
            textField_quantidade.setText("Quantidade");
        }
    }

    public void limpar(){
        textField_quantidade.setText("Quantidade");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.RemoveQuantidade(Integer.parseInt(textField_quantidade.getText()),JComboBox_nome.getSelectedItem()+"");
            limpar();
            setComboBoxNome();
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(null, "Número Invalido", "ERRO", JOptionPane.ERROR_MESSAGE);
            
        }catch(Controller.NomeinvalidoExecption erro){
            JOptionPane.showMessageDialog(null, "Nome Invalido", "ERRO", JOptionPane.ERROR_MESSAGE);
            
        }catch(Controller.NomeNaoEncontradoException erro){
            JOptionPane.showMessageDialog(null, "Nome Não Encontrado", "ERRO", JOptionPane.ERROR_MESSAGE);
        }catch(Controller.QuantidadeMaiorQueONumeroEmEstoqueException erro){
            JOptionPane.showMessageDialog(null, "A quantidade não pode ser maior que o número de produtos em estoque", "ERRO", JOptionPane.ERROR_MESSAGE);
        }catch(Controller.QuantidadeMenorQue0Exception erro){
            JOptionPane.showMessageDialog(null, "A quantidade não deve ser menor ou igual a 0!", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
        controller.getJanela().preencheCorpoPaginaEstoque(controller.getProdutos(), false);
    }
}
