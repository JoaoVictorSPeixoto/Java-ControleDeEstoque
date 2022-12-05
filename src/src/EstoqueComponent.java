package src;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.MouseListener;


public class EstoqueComponent extends JPanel implements MouseListener{

    private Icon icone_fechar = new ImageIcon(getClass().getResource("/img/fechar.png"));
    private Icon icone_visualizar = new ImageIcon(getClass().getResource("/img/olho.png"));
    private JLabel label_nome;
    private JLabel label_quantidade;
    private JLabel label_fechar;
    private JLabel label_visualizar;
    private Controller controler = Controller.istancia();
    private Janela janela;
    private Produto produto;

    public EstoqueComponent(Produto produto){
        setLayout(new FlowLayout(FlowLayout.CENTER,20,0));
        setSize(new Dimension(260,30));
        this.label_nome = new JLabel(produto.getNome());
        this.label_quantidade = new JLabel(produto.getQuantidade()+"");
        this.label_fechar = new JLabel(icone_fechar);
        this. label_visualizar = new JLabel(icone_visualizar);

        label_nome.setPreferredSize(new Dimension(50,30));
        label_quantidade.setPreferredSize(new Dimension(50,30));
        label_fechar.setPreferredSize(new Dimension(50,30));
        label_visualizar.setPreferredSize(new Dimension(50,30));

        label_nome.setFont(new Font("", 1,15));
        label_quantidade.setFont(new Font("", 1,15));
        label_nome.setToolTipText(produto.getNome());
        
        label_fechar.setEnabled(false);
        label_fechar.addMouseListener(this);
        label_visualizar.addMouseListener(this);

        this.add(label_nome);
        this.add(label_quantidade);
        this.add(label_fechar);
        this.add(label_visualizar);
        this.produto = produto;
    }

    public JLabel getFechar(){
        return label_fechar;
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if(e.getComponent().equals(label_fechar)){
            if(e.getComponent().isEnabled()){
                controler.deletar(label_nome.getText());
                JOptionPane.showMessageDialog(label_fechar, "Produto excluido com sucesso!", "Exclusão de produto", JOptionPane.DEFAULT_OPTION);
                janela = Janela.instaciaJanelaMain();
                janela.getCorpo_pagina_saida().setComboBoxNome();
            }
        }else if(e.getComponent().equals(label_visualizar)){
            janela = Janela.instaciaJanelaMain();
            janela.VisualizarProduto(produto);
            
        }  
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}

   
}
