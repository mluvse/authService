package by.mluvse.authService.service;

import by.mluvse.authService.repository.CredentialsRepository;
import by.mluvse.authService.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {
    private final CredentialsRepository repository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findByLogin(login).map(CustomUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException(login));
    }
}
