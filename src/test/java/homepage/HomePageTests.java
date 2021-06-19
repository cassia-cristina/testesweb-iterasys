package homepage;

import static org.hamcrest.MatcherAssert.assertThat;


import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.CarrinhoPage;
import pages.LoginPage;
import pages.ModalProdutoPage;
import pages.ProdutoPage;

public class HomePageTests extends BaseTests {

	@Test
	public void testContarProdutos_oitoProdutosDiferentes() {
		carregarPaginaInicial();
		assertThat(homePage.contarProdutos(), is(8));
	}
	
	@Test
	public void testValidarCarrinhoZerado_zeroItensNoCarrinho() {
		int produtosNoCarrinho = homePage.obterQuantidadeProdutosNoCarrinho();
		assertThat(produtosNoCarrinho, is(0));
		
	}
	
	ProdutoPage produtoPage;
	String nomeProduto_ProdutoPage;
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
		String precoProduto_ProdutoPage = produtoPage.obterPrecoProduto();
		
		System.out.println(nomeProduto_ProdutoPage);
		System.out.println(precoProduto_ProdutoPage);
		
		assertThat(nomeProduto_HomePage.toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		assertThat(precoProduto_HomePage, is(precoProduto_ProdutoPage));
		
	}
	
	LoginPage loginPage;
	@Test
	public void testLoginComSucesso_UsuarioLogado() {
		loginPage =  homePage.clicarBotaoSignIn();
		loginPage.preencherEmail("cassia@teste.com");
		loginPage.preencherPassword("123456");
		loginPage.clicarBotaoSignIn();
		assertThat(homePage.estarLogado("Cassia Souza"), is(true));
		carregarPaginaInicial();
	}
	
	ModalProdutoPage modalProdutoPage;
	@Test
	public void incluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {
		String tamanhoProduto = "M";
		String corProduto = "Black";
		int quantidadeProduto = 2;
		String precoProdutoString;
		Double precoProduto;
		String subtotalString;
		Double subtotal;
		Double subtotalCalculado;
		
		//Pré-condição: usuario logado
		if(!homePage.estarLogado("Cassia Souza")) {
			testLoginComSucesso_UsuarioLogado();
		}
		//Teste
		//Selecionando produto
		testValidarDetalhesDoProduto_DescricaoEValorIguais();
		//Selecionando o tamanho
		List<String> listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println("Opção selecionada é: " + listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		produtoPage.selecionarOpcaoDropDown(tamanhoProduto);
		
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println("Opção selecionada é: " + listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		//Selecionando cor
		produtoPage.selecionarCorPreta();		
		//Selecionando quantidade
		produtoPage.alterarQuantidade(quantidadeProduto);
		//Adicionando no carrinho
		modalProdutoPage = produtoPage.clicarBotaoAddToCart();
		
		//Validações
		assertTrue(modalProdutoPage.obterMensagemProdutoAdicionado().endsWith("Product successfully added to your shopping cart"));
		
		precoProdutoString = modalProdutoPage.obterPrecoProduto();
		precoProdutoString = precoProdutoString.replace("$", "");
		precoProduto = Double.parseDouble(precoProdutoString);
		
		subtotalString = modalProdutoPage.obterSubtotal();
		subtotalString = subtotalString.replace("$", "");
		subtotal = Double.parseDouble(subtotalString);
		subtotalCalculado = quantidadeProduto * precoProduto;
		
		assertThat(modalProdutoPage.obterTamanhoProduto(), is(tamanhoProduto));
		assertThat(modalProdutoPage.obterCorProduto(), is(corProduto));
		assertThat(modalProdutoPage.obterQuantidadeProduto(), is(Integer.toString(quantidadeProduto)));
		assertThat(subtotal, is(subtotalCalculado));
		assertThat(modalProdutoPage.obterDescricaoProduto().toUpperCase(), is(nomeProduto_ProdutoPage.toUpperCase()));
		
	}
	
	@Test
	public void irParaCarrinho_InformacoesPersistidas() {
		//Pré-condições:
		//Produto incluido na tela ModalProdutoPage
		incluirProdutoNoCarrinho_ProdutoIncluidoComSucesso();
		
		CarrinhoPage carrinhoPage = modalProdutoPage.clicarBotaoProceedToCheckout();
		
		//Teste
		
		
		
		//Validar todos os elementos da tela
		
		
	}
	
		
	
}
