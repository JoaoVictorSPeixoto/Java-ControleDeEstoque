package src;

//import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;


public class Janela extends JFrame implements ActionListener{
    private JPanel cabecario;
    private Saida  corpo_pagina_saida;
    private JPanel corpo_pagina_dos_produtos;
    private JPanel corpo_pagina_do_estoque;
    private JPanel corpo_vizualizar_produto;
    private ScrollPane scrolPane;
    
    private JButton excluir;
    private JButton button_produtos;
    private JButton button_estoque;
    private JButton button_saida;
    private JButton button_voltar;
    private JButton button_editar;
    private final int LARGURA = 350, ALTURA = 400;

    private MaskFormatter MF_validade;

    private JButton button_adicionar;
    private JButton button_limpar;
    private JTextField textField_nome;
    private JFormattedTextField  textField_quantidade;
    private JFormattedTextField textField_validade;
    private JFormattedTextField margem_de_lucro;
    private JFormattedTextField textField_custo;

    private JTextField textField_nome_produto;
    private JFormattedTextField textField_quantidade_produto;
    private JFormattedTextField textField_validade_produto;
    private JFormattedTextField margem_de_lucro_produto;
    private JFormattedTextField textField_custo_produto;
    private JFormattedTextField textField_preco_de_venda_produto;

    private JPanel botoesEstoqueRodape;
    private JPanel barra_de_descricao_scrolPane;

    private boolean controladorAtivado = false;
    private boolean controleEditarONOFF = false;

    private ArrayList<Produto> produtos;
    private ArrayList<EstoqueComponent> estoqueComponent = new ArrayList<>();

    private static Janela janelaMain;
    private Controller bd;
    // variaveis usadas na hora da editação, como amazenamento de backup dos dados antes do inicio da edição
    private String nome, validade;
    private int quantidade;
    private Double custo, margemDeLucro;
    
    private Janela(String titulo, ArrayList<Produto> produtos){
        super(titulo);
        bd = Controller.istancia();
        this.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
        this.setSize(LARGURA,ALTURA);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.produtos = produtos;

        preenche();

        this.setVisible(true);
        textField_nome.requestFocus();
    }

    public static Janela instaciaJanelaMain(String titulo, ArrayList<Produto> produtos){
        if(janelaMain == null){
            janelaMain = new Janela(titulo,produtos);
        }
        return janelaMain;
    }
    public static Janela instaciaJanelaMain(){
        return janelaMain;
    }


    public void preenche(){
        cabecario = new JPanel();
        cabecario.setPreferredSize(new Dimension(LARGURA,50));
        cabecario.setLayout(new FlowLayout(FlowLayout.CENTER,5,15));
        corpo_vizualizar_produto = new JPanel();
        corpo_pagina_saida = new Saida(ALTURA-50, LARGURA);
        
        
        try {
            preencheCorpoPaginaProdutos();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setEstoque();
        preencheCorpoPaginaEstoque(produtos,false);
        preencheBotoesEstoqueRodaPe();
        preencheJpanelProdutos();

        button_produtos = new JButton("Produtos");
        button_estoque = new JButton("Estoque");
        button_saida = new JButton("Saida");
        button_estoque.setFocusPainted(false);
        button_produtos.setFocusPainted(false);
        button_saida.setFocusPainted(false);

        cabecario.add(button_produtos);
        cabecario.add(button_estoque);
        cabecario.add(button_saida);
        button_produtos.addActionListener(this);
        button_estoque.addActionListener(this);
        button_saida.addActionListener(this);
        this.add(cabecario);
        this.add(scrolPane);
        this.add(corpo_pagina_dos_produtos);
        this.add(botoesEstoqueRodape);
        this.add(corpo_vizualizar_produto);
        this.add(corpo_pagina_saida);
    }

    public void VisualizarProduto(Produto produto){
        corpo_pagina_dos_produtos.setVisible(false);
        scrolPane.setVisible(false);
        botoesEstoqueRodape.setVisible(false);
        cabecario.setVisible(false);
        textField_nome_produto.setText(produto.getNome());
        textField_quantidade_produto.setText(produto.getQuantidade()+"");
        textField_validade_produto.setText(produto.getValidade());
        margem_de_lucro_produto.setText(produto.getMargem_de_lucro()+"%");
        textField_custo_produto.setText(produto.getCusto()+"");
        textField_preco_de_venda_produto.setText(produto.getPreco_de_venda()+"");
        corpo_vizualizar_produto.setVisible(true);
        
    
    }

    public void preencheJpanelProdutos(){
        JPanel secao_nome = new JPanel();
        JPanel secao_quantidade = new JPanel();
        JPanel secao_validade = new JPanel();
        JPanel secao_margem_de_lucro = new JPanel();
        JPanel secao_custo = new JPanel();
        JPanel secao_preco_de_venda = new JPanel();
       

        secao_nome.setPreferredSize(new Dimension((int)getSize().getWidth(),40));
        secao_nome.setLayout(new FlowLayout(FlowLayout.CENTER));

        secao_quantidade.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_quantidade.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        secao_validade.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_validade.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        secao_margem_de_lucro.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_margem_de_lucro.setPreferredSize(new Dimension((int)getSize().getWidth(),40));
        
        secao_custo.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_custo.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        secao_preco_de_venda.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_preco_de_venda.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        JPanel formulario = new JPanel();
        formulario.setPreferredSize(new Dimension(LARGURA,250));
        formulario.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));

        textField_nome_produto = new JTextField();
        textField_nome_produto.setPreferredSize(new Dimension(200,20));
        textField_nome_produto.setEditable(false);

        textField_quantidade_produto = new JFormattedTextField();
        textField_quantidade_produto.setPreferredSize(new Dimension(28,20));
        textField_quantidade_produto.setEditable(false);

        textField_validade_produto = new JFormattedTextField();
        textField_validade_produto.setPreferredSize(new Dimension(80,20));
        textField_validade_produto.setEditable(false);
        MF_validade.install(textField_validade);

        margem_de_lucro_produto = new JFormattedTextField();
        margem_de_lucro_produto.setPreferredSize(new Dimension(50,20));
        margem_de_lucro_produto.setEditable(false);

        textField_custo_produto = new JFormattedTextField();
        textField_custo_produto.setPreferredSize(new Dimension(50,20));
        textField_custo_produto.setEditable(false);

        textField_preco_de_venda_produto = new JFormattedTextField();
        textField_preco_de_venda_produto.setPreferredSize(new Dimension(50,20));
        textField_preco_de_venda_produto.setEditable(false);

        //Adição dos componetes do formulario aos seus respectivos paineis especificos
        secao_nome.add(new JLabel("Nome"));
        secao_nome.add(textField_nome_produto);
        
        secao_quantidade.add(new JLabel("Quantidade"));
        secao_quantidade.add(textField_quantidade_produto);

        secao_validade.add(new JLabel("Validade"));
        secao_validade.add(textField_validade_produto);

        secao_margem_de_lucro.add(new JLabel("Margem de lucro"));
        secao_margem_de_lucro.add(margem_de_lucro_produto);

        secao_custo.add(new JLabel("Custo"));
        secao_custo.add(textField_custo_produto);

        secao_preco_de_venda.add(new JLabel("Preço de venda"));
        secao_preco_de_venda.add(textField_preco_de_venda_produto);

        //Cria e define caracteristicas do painel BOTÕES
        JPanel botoes = new JPanel();
        botoes.setPreferredSize(new Dimension(LARGURA,50));
        botoes.setLayout(new FlowLayout(FlowLayout.CENTER,3,8));
        
        button_voltar = new JButton("Voltar");
        button_voltar.addActionListener(this);
        button_editar = new JButton("Editar");
        button_editar.addActionListener(this);

        button_voltar.setFocusPainted(false);
        button_editar.setFocusPainted(false);

        botoes.add(button_voltar);
        botoes.add(button_editar);

        //Adição de cada painel das seçãos do formulario ao painel FORMULARIO
        formulario.add(secao_nome);
        formulario.add(secao_quantidade);
        formulario.add(secao_validade);
        formulario.add(secao_custo);
        formulario.add(secao_margem_de_lucro);
        formulario.add(secao_preco_de_venda);

        
        corpo_vizualizar_produto.setLayout(new FlowLayout(FlowLayout.CENTER));
        corpo_vizualizar_produto.setPreferredSize(new Dimension(LARGURA,ALTURA));

        corpo_vizualizar_produto.add(formulario);
        corpo_vizualizar_produto.add(botoes);
    }
    
    public void preencheCorpoPaginaProdutos() throws ParseException{
        //Paineis para cada seção do formulario, feito para melhor organização
        JPanel secao_nome = new JPanel();
        JPanel secao_quantidade = new JPanel();
        JPanel secao_validade = new JPanel();
        JPanel secao_margem_de_lucro = new JPanel();
        JPanel secao_custo = new JPanel();
        MF_validade = new MaskFormatter("##/##/####");
        //Definição das caracteristicas de cada painel
        secao_nome.setPreferredSize(new Dimension((int)getSize().getWidth(),40));
        secao_nome.setLayout(new FlowLayout(FlowLayout.CENTER));

        secao_quantidade.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_quantidade.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        secao_validade.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_validade.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        secao_margem_de_lucro.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_margem_de_lucro.setPreferredSize(new Dimension((int)getSize().getWidth(),40));
        
        secao_custo.setLayout(new FlowLayout(FlowLayout.LEFT,30,0));
        secao_custo.setPreferredSize(new Dimension((int)getSize().getWidth(),40));

        //Criação e definição da caracteristicas do painel FORMULARIO
        JPanel formulario = new JPanel();
        formulario.setPreferredSize(new Dimension(LARGURA,(int)((getSize().getHeight()) - (getSize().getHeight()/2.8))));
        formulario.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
        
        //Instancias os componetes do formulario
        textField_nome = new JTextField();
        textField_nome.setPreferredSize(new Dimension(200,20));
        

        textField_quantidade = new JFormattedTextField("0");
        textField_quantidade.setPreferredSize(new Dimension(28,20));

        textField_validade = new JFormattedTextField("00/00/0000");
        textField_validade.setPreferredSize(new Dimension(80,20));
        MF_validade.install(textField_validade);

        margem_de_lucro = new JFormattedTextField("00");
        margem_de_lucro.setPreferredSize(new Dimension(50,20));

        textField_custo = new JFormattedTextField("00");
        textField_custo.setPreferredSize(new Dimension(50,20));

        //Adição dos componetes do formulario aos seus respectivos paineis especificos
        secao_nome.add(new JLabel("Nome"));
        secao_nome.add(textField_nome);
        
        secao_quantidade.add(new JLabel("Quantidade"));
        secao_quantidade.add(textField_quantidade);

        secao_validade.add(new JLabel("Validade"));
        secao_validade.add(textField_validade);

        secao_margem_de_lucro.add(new JLabel("Margem de lucro"));
        secao_margem_de_lucro.add(margem_de_lucro);

        secao_custo.add(new JLabel("Custo"));
        secao_custo.add(textField_custo);

        //Adição de cada painel das seçãos do formulario ao painel FORMULARIO
        formulario.add(secao_nome);
        formulario.add(secao_quantidade);
        formulario.add(secao_validade);
        formulario.add(secao_custo);
        formulario.add(secao_margem_de_lucro);
        


        //Cria e define caracteristicas do painel BOTÕES
        JPanel botoes = new JPanel();
        botoes.setPreferredSize(new Dimension(LARGURA,(int)(getSize().getHeight()/2.8)));
        botoes.setLayout(new FlowLayout(FlowLayout.CENTER,3,8));
        
        button_limpar = new JButton("limpar");
        button_limpar.addActionListener(this);
        button_adicionar = new JButton("Adicionar");
        button_adicionar.addActionListener(this);

        button_limpar.setFocusPainted(false);
        button_adicionar.setFocusPainted(false);

        botoes.add(button_limpar);
        botoes.add(button_adicionar);

        corpo_pagina_dos_produtos = new JPanel();
        corpo_pagina_dos_produtos.setPreferredSize(new Dimension(LARGURA,ALTURA-50));
        corpo_pagina_dos_produtos.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        
        
        corpo_pagina_dos_produtos.add(formulario);
        corpo_pagina_dos_produtos.add(botoes);

        corpo_pagina_dos_produtos.setVisible(true);
        
    }

    public void setEstoque(){
        corpo_pagina_do_estoque = new JPanel();
        corpo_pagina_do_estoque.setSize(new Dimension(LARGURA-50,ALTURA-50));
        corpo_pagina_do_estoque.setLayout(new FlowLayout(FlowLayout.CENTER));
        corpo_pagina_do_estoque.setVisible(true);
        barra_de_descricao_scrolPane = new JPanel();
        barra_de_descricao_scrolPane.setLayout(new FlowLayout(FlowLayout.CENTER,25,0));
        barra_de_descricao_scrolPane.setPreferredSize(new Dimension(500,30));
        barra_de_descricao_scrolPane.add(new JLabel("Nome"));
        barra_de_descricao_scrolPane.add(new JLabel("Quantidade"));
        barra_de_descricao_scrolPane.add(new JLabel("Excluir"));
        barra_de_descricao_scrolPane.add(new JLabel("Visualizar"));
        scrolPane = new ScrollPane();
        scrolPane.setSize(new Dimension(LARGURA-20,ALTURA-135));
        scrolPane.add(barra_de_descricao_scrolPane);
        scrolPane.setVisible(false);
    }

    public void setBotaoFecharComponentesFalse(boolean ativa_desativa){
        for (int i = 0; i < estoqueComponent.size(); i++) {
            if(ativa_desativa){
                estoqueComponent.get(i).getFechar().setEnabled(true);
            }else{
                estoqueComponent.get(i).getFechar().setEnabled(false);
            }
            
        }
        
    }

    public void preencheCorpoPaginaEstoque(ArrayList<Produto> produtos, boolean ativaExclusaoProdutos){
        corpo_pagina_do_estoque.removeAll();
        estoqueComponent.removeAll(estoqueComponent);
        for (int i = 0; i < produtos.size(); i++) {
            EstoqueComponent ec = new EstoqueComponent(produtos.get(i));
            estoqueComponent.add(ec);
        }
        corpo_pagina_do_estoque.add(barra_de_descricao_scrolPane);
        for (int i = 0; i < estoqueComponent.size(); i++) {
            if(ativaExclusaoProdutos){
                setBotaoFecharComponentesFalse(ativaExclusaoProdutos);
            }
            corpo_pagina_do_estoque.add(estoqueComponent.get(i));
        }
        
        corpo_pagina_do_estoque.setPreferredSize(new Dimension(LARGURA-50,(produtos.size()*39)));
        scrolPane.add(corpo_pagina_do_estoque);
        
    }
    
    public void preencheBotoesEstoqueRodaPe(){
        botoesEstoqueRodape = new JPanel();
        botoesEstoqueRodape.setPreferredSize(new Dimension(LARGURA,60));
        botoesEstoqueRodape.setLayout(new FlowLayout(FlowLayout.CENTER,3,8));

        excluir = new JButton("Excluir OFF");
        excluir.setFocusPainted(false);

        excluir.addActionListener(this);

        botoesEstoqueRodape.add(excluir);
        botoesEstoqueRodape.setVisible(false);
    }

    public void limpar(){
        textField_nome.setText("");
        textField_quantidade.setText("0");
        
        try {
            
            MF_validade.setMask("##/##/####");
            MF_validade.install(textField_validade);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textField_validade.setText("00/00/0000");
        margem_de_lucro.setText("00");

        textField_custo.setText("00");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(button_estoque)){
            corpo_pagina_dos_produtos.setVisible(false);
            scrolPane.repaint();
            corpo_pagina_do_estoque.repaint();
            scrolPane.setVisible(true);
            botoesEstoqueRodape.setVisible(true);
        }else if(e.getSource().equals(button_produtos)){
            corpo_pagina_dos_produtos.setVisible(true);
            scrolPane.setVisible(false);
            botoesEstoqueRodape.setVisible(false);
            setBotaoFecharComponentesFalse(false);
            excluir.setText("Excluir OFF");
            controladorAtivado = false;
        }else if(e.getSource().equals(button_limpar)){
            limpar();
        }else if(e.getSource().equals(button_adicionar)){
            
            Produto produto = new Produto();
            produto.setNome(textField_nome.getText());
            produto.setValidade(textField_validade.getText());
            try {
                produto.setQuantidade(Integer.parseInt(textField_quantidade.getText()));
                produto.setCusto(Double.parseDouble(textField_custo.getText()));
                produto.setMargem_de_lucro(Double.parseDouble(margem_de_lucro.getText()));
            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(null, "Dados Invalidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                limpar();
            }
           
            Double precoVenda = produto.getCusto() / ((100 - produto.getMargem_de_lucro())/100);
            String precoDeVenda = String.format("%.2f",precoVenda);
            precoDeVenda =  precoDeVenda.replaceAll(",", ".");
            produto.setPreco_de_venda(Double.parseDouble(precoDeVenda));

            try {
                
                bd.salvar(produto);
                JOptionPane.showMessageDialog(null,"Produto Cadastrado com sucesso\n Preço de venda: "+produto.getPreco_de_venda());
            preencheCorpoPaginaEstoque(bd.getProdutos(),false);
            } catch (Controller.CampoNomeInvalidoException e1) {
                JOptionPane.showMessageDialog(null, "Nome Invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Controller.CampoQuantidadeInvalidoException e1) {
                JOptionPane.showMessageDialog(null, "Quantidade Invalida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Controller.CampoValidadeInvalidoException e1) {
                JOptionPane.showMessageDialog(null, "Validade Invalida!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Controller.CampoCustoInvalidoException e1) {
                JOptionPane.showMessageDialog(null, "Custo Invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Controller.CampoMargemDeLucroInvalidoException e1) {
                JOptionPane.showMessageDialog(null, "Margem de Lucro Invalida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
            corpo_pagina_saida.setComboBoxNome();
            limpar();
            
        }else if(e.getSource().equals(excluir)){
            
            if(controladorAtivado){
                for (int i = 0; i < estoqueComponent.size(); i++) {
                    estoqueComponent.get(i).getFechar().setEnabled(false);
                }
                excluir.setText("Excluir OFF");
                controladorAtivado = false;
            }else{
                for (int i = 0; i < estoqueComponent.size(); i++) {
                    estoqueComponent.get(i).getFechar().setEnabled(true);
                }
                excluir.setText("Excluir ON");
                controladorAtivado = true;
            }
            
        }else if(e.getSource().equals(button_voltar)){
            
            

            if(controleEditarONOFF == true){
                Produto produto = new Produto();
                Double margem_de_lucro, custo;

                margem_de_lucro = Double.parseDouble(margem_de_lucro_produto.getText().replaceAll("%",""));
                custo = Double.parseDouble(textField_custo_produto.getText());

                produto.setNome(textField_nome_produto.getText());
                produto.setQuantidade(Integer.parseInt(textField_quantidade_produto.getText()));
                produto.setValidade(textField_validade_produto.getText());
                produto.setCusto(custo);
                produto.setMargem_de_lucro(margem_de_lucro);

                Double novoPrecoDeVenda = custo / ((100 - margem_de_lucro)/100);
                String precoDeVenda = String.format("%.2f",novoPrecoDeVenda);
                precoDeVenda =  precoDeVenda.replaceAll(",", ".");
                produto.setPreco_de_venda(Double.parseDouble(precoDeVenda));

               
                controleEditarONOFF = false;
                button_editar.setText("Editar");
                button_voltar.setText("Voltar");
                textField_preco_de_venda_produto.setText(precoDeVenda);
                bd.Atualizar(produto);
                
            }else{
                
                scrolPane.setVisible(true);
                botoesEstoqueRodape.setVisible(true);
                cabecario.setVisible(true);
                corpo_vizualizar_produto.setVisible(false);
            }

            textField_nome_produto.setEditable(false);
            textField_quantidade_produto.setEditable(false);
            textField_custo_produto.setEditable(false);
            textField_validade_produto.setEditable(false);
            margem_de_lucro_produto.setEditable(false);
        }else if(e.getSource().equals(button_editar)){
           
            if(controleEditarONOFF == false){
                button_editar.setText("Cancelar");
                button_voltar.setText("Salvar");
                textField_nome_produto.setEditable(true);
                textField_quantidade_produto.setEditable(true);
                textField_custo_produto.setEditable(true);
                textField_validade_produto.setEditable(true);
                margem_de_lucro_produto.setEditable(true);
                
                nome = textField_nome_produto.getText();
                quantidade = Integer.parseInt(textField_quantidade_produto.getText());
                validade = textField_validade_produto.getText();
                custo = Double.parseDouble(textField_custo_produto.getText());
                margemDeLucro = Double.parseDouble(margem_de_lucro_produto.getText().replaceAll("%",""));

                controleEditarONOFF = true;
            }else{
                button_editar.setText("Editar");
                button_voltar.setText("Voltar");

                textField_nome_produto.setText(nome);
                textField_quantidade_produto.setText(quantidade+"");
                textField_validade_produto.setText(validade);
                textField_custo_produto.setText(custo+"");
                margem_de_lucro_produto.setText(margemDeLucro+"%");

                textField_nome_produto.setEditable(false);
                textField_quantidade_produto.setEditable(false);
                textField_custo_produto.setEditable(false);
                textField_validade_produto.setEditable(false);
                margem_de_lucro_produto.setEditable(false);

                controleEditarONOFF = false;

            }
            
        }else if(e.getSource().equals(button_saida)){
            corpo_pagina_dos_produtos.setVisible(false);
            scrolPane.setVisible(false);
            botoesEstoqueRodape.setVisible(false);
            setBotaoFecharComponentesFalse(false);
            excluir.setText("Excluir OFF");
            controladorAtivado = false;
            corpo_vizualizar_produto.setVisible(false);
        }
        
    }

    public ArrayList<EstoqueComponent> getEstoqueComponent(){
        return estoqueComponent;
    }

	public Saida getCorpo_pagina_saida() {
		return corpo_pagina_saida;
	}
    
}
