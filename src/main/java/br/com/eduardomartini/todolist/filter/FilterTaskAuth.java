package br.com.eduardomartini.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.eduardomartini.todolist.user.IuserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IuserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            var servletPath = request.getServletPath();
            if(servletPath.startsWith("/tasks")) {

        // Pegar a autenticação (Usuário e senha)
        var authorization = request.getHeader("Authorization");

        var authEncoded = authorization.substring("Basic".length()).trim();// cortando o basic e retirando o espaço

        byte[] authDecode = Base64.getDecoder().decode(authEncoded);

        var authString = new String(authDecode);

        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        // Validar usuário
        var user = this.userRepository.findByUsername(username);
        if (user == null) {
            response.sendError(401);
        }
        else {
            // Validar senha
            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            // Segue viagem
            if (passwordVerify.verified) {
                request.setAttribute("idUser", user.getId());                                          //request = tudo que está vindo -- response == estamos enviando
                filterChain.doFilter(request, response);
            } else {
                response.sendError(401);
            }

            

            }
         }else{
            filterChain.doFilter(request, response);
         }

    }
}
