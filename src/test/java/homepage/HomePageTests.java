package homepage;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;

import base.BaseTests;
import pages.LoginPage;
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
	@Test
	public void testValidarDetalhesDoProduto_DescricaoEValorIguais() {
		int indice = 0;
		String nomeProduto_HomePage = homePage.obterNomeProduto(indice);
		String precoProduto_HomePage = homePage.obterPrecoProduto(indice);
		
		System.out.println(nomeProduto_HomePage);
		System.out.println(precoProduto_HomePage);
		
		produtoPage = homePage.clicarProduto(indice);
		String nomeProduto_ProdutoPage = produtoPage.obterNomeProduto();
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
	
	@Test
	public void incluirProdutoNoCarrinho_ProdutoIncluidoComSucesso() {
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
		produtoPage.selecionarOpcaoDropDown("M");
		
		listaOpcoes = produtoPage.obterOpcoesSelecionadas();
		System.out.println("Opção selecionada é: " + listaOpcoes.get(0));
		System.out.println("Tamanho da lista: " + listaOpcoes.size());
		
		//Selecionando cor
		produtoPage.selecionarCorPreta();
		
		//Selecionando quantidade
		produtoPage.alterarQuantidade(2);
		
		//Adicionando no carrinho
		
	}
	
	
	
	
	
}


