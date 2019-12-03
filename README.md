# Lib Gerenciadora de Downloads

Esse projeto foi criado visando nota para avaliação de programação avançada.

- O app chama a única instância da fachada (DownloadFacade), que por sua vez inicia todos os controladores;
- Após isso o app pede pra fachada enviar o controlador do download (que é o Download Controller);
- Essa sequência de chamadas retorna por fim uma interface, o IDownloadController;
- Essa interface possui nela um único método até o momento: download;
- O método "download" precisa de 4 parâmetros: o contexto do app (Context), a url, o nome que se deseja por no arquivo e por fim a extensão do arquivo;

Agora o que acontece de fato dentro da lib:
- No DownloadController, ao implementar o método "download" da interface IDownloadController faz algumas coisas:
- Primeiro ele prepara uma Intent, onde ele coloca todos os quatro parâmetros que vamos precisar para fazer o download;
- Após preparar o intent, ele chama um serviço através do método "startService", onde ele passa a intent criada e a classe de serviço que cuidará da intent (DownloadService).
- No DownloadService, herdamos de uma classe chamada "Service" (daí o motivo de eu ter colocado o nome assim);
- Para o DownloadService ser reconhecido como um serviço real, declaro ele no Manifesto da própria lib (funciona de forma semelhante aos Activities, que também precisamos declarar no Manifesto);
- O ciclo de vida dessa classe de serviço funciona da seguinte maneira:
- Quando é chamado a primeira vez no app, ele passa pelo método onCreate (é realmente apenas uma única vez quando se abre o app);
- Depois do onCreate, ele passa pelo onStartCommand. Caso o onCreate já tenha sido chamado outra vez enquanto o app tava aberto, ele já passa direto pro onStartCommand;
- No onStartCommand, ele de fato recebe o Intent que foi enviado e lá preparo uma Mensagem (uma classe chamada Message) que receberá como extras os parâmetros que quero pro download (a url, o nome do arquivo e a extensão que ele terá);
- Após preparar a Mensagem, envio para o ServiceHandler, que foi uma classe criada dentro do próprio DownloadService, onde herda de Handler, que é uma classe semelhante ao Thread, mas com a principal diferença de poder conversar com a thread principal do app que está rodando;
- No ServiceHandler eu recebo a Mensagem que criei e preparo ela para um método que fará a comunicação com o DownloadManager, um método que chamei apenas de "download;
- No método "download" do DownloadService eu recebo os parâmetros que são: url, nome e extensão, sendo todos apenas Strings;
- Após isso precisei preparar o DownloadManager.Request, que é uma classe interna do DownloadManager que preciso instanciar para preparar meu pedido de download;
- Nesse request (chamei apenas assim mesmo a instância), preciso obrigatoriamente passar a url em formato Uri;
- Como configurações extras, preparei um caminho (com Uri.fromFile) e também configurei para que aparecesse a notificação do fim do download;
- Após preparar o request, pedi para o DownloadManager enfileirar ele para ser feito o download (DownloadManager.enqueue(request));

Extras:
- Pra preparar o caminho final do download (e decidir para qual pasta enviar o download), criei uma classe helper, chamada apenas de DirectoryTypeHelper;
- Essa classe não pode ser instanciada, pois a única intenção dessa é ajudar a decidir qual é o nome da pasta através do nome do tipo de arquivo que ela recebe;
- Ela recebe apenas uma String, que é a extensão do arquivo;
- Tanto faz a pessoa mandar a String sendo, por exemplo, ".mp3" ou "mp3". Ela cuida desse ".";
- Ela consulta sua lista interna de referência pra ver qual categoria a extensão se encaixa;
- Ao descobrir a categoria, ela retorna o nome da mesma para quem a chamou;
- Caso ela não descubra a categoria, ela também retorna o nome da categoria, sendo no caso o "Downloads" (porque acredito que seria a pasta mais geral para salvar arquivos diversos de downloads :P).