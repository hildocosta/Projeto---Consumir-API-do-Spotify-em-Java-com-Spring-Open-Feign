

```markdown
# 🎵 Projeto - Consumir API do Spotify em Java com Spring OpenFeign  

🚀 Este projeto demonstra como consumir a API do Spotify utilizando **Spring Boot** e o cliente HTTP **Spring OpenFeign**. Ele é ideal para quem deseja aprender como integrar aplicações Java com APIs externas, gerenciar autenticação OAuth2, e explorar as melhores práticas para consumir e processar dados de serviços RESTful.  

---

## 📚 O que você aprenderá  

- Configurar um projeto Java para consumir APIs externas usando Spring OpenFeign.
- Criar um projeto inicial no [Spring Initializr](https://start.spring.io/).
- Configurar credenciais para acesso à API do Spotify no [Spotify Developer Dashboard](https://developer.spotify.com/dashboard/).
- Explorar as APIs do Spotify.
- Implementar autenticação OAuth2 para obter tokens de acesso.
- Criar FeignClients para autenticação e consumo de dados do Spotify.
- Disponibilizar os dados consumidos em uma API própria.
- Realizar requisições RESTful para acessar informações como playlists, artistas e álbuns.
- Organizar o código com boas práticas e padrões arquiteturais.

---

## 📋 Etapas do Projeto  

### 1. Criar um novo projeto no **Spring Initializr**  

**Configurações do projeto:**  
- **Linguagem:** Java  
- **Ferramenta de Build:** Maven  
- **Versão do Spring Boot:** 3.4.1  
- **Grupo:** `com.hildo.costa`  
- **Artefato:** `spotify-api`  
- **Nome do Projeto:** `spotify-api`  
- **Descrição:** `Consumir API do Spotify com Spring OpenFeign`  
- **Tipo de Empacotamento:** `JAR`  
- **Versão do Java:** 21  

**Dependências:**  
- **Spring Web**  
- **Spring OpenFeign**  

Após configurar, clique em **Generate** para baixar o projeto.

### 2. Importar o projeto no IntelliJ IDEA  

- Importe o projeto como um **projeto Maven**.  
- Verifique se todas as dependências foram baixadas corretamente.

### 3. Estrutura do Projeto  

Organize o projeto como abaixo:  

```plaintext
src/main/java  
└── com  
    └── hildo  
        └── costa  
            └── spotify_api  
                ├── SpotifyApiApplication.java
                ├── controller  
                │   └── AlbumController.java  
                ├── client  
                │   ├── Album.java  
                │   ├── AlbumResponse.java  
                │   ├── AlbumSpotifyClient.java  
                │   ├── AlbumWrapper.java  
                │   ├── AuthSpotifyClient.java  
                │   ├── GoogleClient.java  
                │   ├── LoginRequest.java  
                │   └── LoginResponse.java  
```  

### 4. Configurar o acesso à API do Spotify  

1. **Registrar sua aplicação no Spotify Developer Dashboard:**  
   - Acesse [Spotify for Developers](https://developer.spotify.com/dashboard/).  
   - Crie um novo aplicativo e obtenha as **credenciais de cliente (Client ID e Client Secret)**.  

2. **Implementar autenticação OAuth2:**  
   - O cliente `AuthSpotifyClient` foi criado para realizar autenticação usando o fluxo Client Credentials.
   - A classe `LoginRequest` contém os parâmetros necessários para autenticação.
   - A classe `LoginResponse` armazena o token de acesso recebido.

---

## 🛠️ Implementação  

### 1. **Classe Principal - SpotifyApiApplication.java**  

```java
@SpringBootApplication
@EnableFeignClients
public class SpotifyApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpotifyApiApplication.class, args);
    }
}
```

Esta classe inicializa o projeto Spring Boot e habilita o uso de FeignClients.

### 2. **Controller - AlbumController.java**  

O controller expõe um endpoint `/spotify/api/albums` que consome os álbuns mais recentes do Spotify.

```java
@RestController
@RequestMapping("/spotify/api")
public class AlbumController {
    private final AuthSpotifyClient authSpotifyClient;
    private final AlbumSpotifyClient albumSpotifyClient;

    public AlbumController(AuthSpotifyClient authSpotifyClient,
                           AlbumSpotifyClient albumSpotifyClient) {
        this.authSpotifyClient = authSpotifyClient;
        this.albumSpotifyClient = albumSpotifyClient;
    }

    @GetMapping("/albums")
    public ResponseEntity<List<Album>> getAlbums() {
        var request = new LoginRequest(
            "client_credentials",
            "CLIENT_ID",
            "CLIENT_SECRET"
        );
        var token = authSpotifyClient.login(request).getAccessToken();
        var response = albumSpotifyClient.getReleases("Bearer " + token);
        return ResponseEntity.ok(response.getAlbums().getItems());
    }
}
```

### 3. **FeignClient - AuthSpotifyClient.java**  

```java
@FeignClient(name = "AuthSpotifyClient", url = "https://accounts.spotify.com")
public interface AuthSpotifyClient {
    @PostMapping(value = "/api/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponse login(@RequestBody LoginRequest loginRequest);
}
```

Este cliente realiza autenticação no Spotify.

### 4. **FeignClient - AlbumSpotifyClient.java**  

```java
@FeignClient(name = "AlbumSpotifyClient", url = "https://api.spotify.com")
public interface AlbumSpotifyClient {
    @GetMapping(value = "/v1/browse/new-releases")
    AlbumResponse getReleases(@RequestHeader("Authorization") String authorization);
}
```

Este cliente consome os dados de álbuns mais recentes.

---

## 🔗 Estruturas de Dados  

### **Album.java**  

```java
public class Album {
    private String id;
    private String name;
    private String releaseDate;
    // Getters e Setters
}
```

### **AlbumWrapper.java**  

```java
public class AlbumWrapper {
    private List<Album> items;
    // Getters e Setters
}
```

### **AlbumResponse.java**  

```java
public class AlbumResponse {
    private AlbumWrapper albums;
    // Getters e Setters
}
```

### **LoginRequest.java**  

```java
public class LoginRequest {
    private String grantType;
    private String clientId;
    private String clientSecret;
    // Construtores, Getters e Setters
}
```

### **LoginResponse.java**  

```java
public class LoginResponse {
    private String accessToken;
    // Getters e Setters
}
```

---

## 🚀 Execução  

1. Configure suas credenciais do Spotify no `AlbumController`.
2. Inicie a aplicação com o comando:  

```bash
mvn spring-boot:run
```

3. Acesse o endpoint:  
```
GET http://localhost:8080/spotify/api/albums
```

---

## 📦 Dependências  

As dependências principais utilizadas neste projeto são:  

- **Spring Boot 3.4.1**  
- **Spring Web**  
- **Spring OpenFeign**  
- **Jackson** (para manipulação de JSON)

---

## 📝 Observações  

Certifique-se de substituir `CLIENT_ID` e `CLIENT_SECRET` pelas suas credenciais do Spotify. Não exponha essas informações publicamente.  

---

## 📜 Licença  

Este projeto é de uso livre para fins educacionais.
```

