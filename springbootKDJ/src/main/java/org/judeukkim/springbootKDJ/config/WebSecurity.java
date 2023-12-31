package org.judeukkim.springbootKDJ.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //스프링 IOC Container에게 해당 클래스를 Bean 구성 Class임을 알려주는 것이다.
@EnableWebSecurity //Spring Security를 활성화 시킵니다.
@RequiredArgsConstructor
public class WebSecurity {
                                       //외부에서 로컬의 특정 폴더에 접근
                                        //WebMvcConfigurer 인터페이스는 스프링의 MVC 구성에 관련된 다양한 설정을 추가하거나 커스터마이징하기 위해 사용됩니다.
                                        // 이 인터페이스를 구현하여 필요한 메서드를 오버라이드하면, 웹 애플리케이션의 다양한 구성 요소에 대한 설정을 할 수 있습니다.

    private final UserDetailSecurity userDetailSecurity;
    private final SimpleUrlAuthenticationFailureHandler customFailHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable(); //페이지 보안 설정  Exception 예외 처리 필수
        http.userDetailsService(userDetailSecurity);  //<< 유저디테일서비스만들어놓은것

        //권한설정
        http.authorizeHttpRequests()
                .antMatchers("/","/login","/join").permitAll() //모든사용자 접근가능
                .antMatchers("/member/**").authenticated()                      //로그인시 접근가능
                .antMatchers("/member/**").hasAnyRole("MEMBER","ADMIN")  //MEMBER,ADMIN 권한만접근가능
                .antMatchers("/admin/**").hasRole("ADMIN");                    //ADMIN 권한만 접근가능

        http.formLogin()                                //form Login 설정을 진행합니다.
                .loginPage("/login")                    //로그인 요청시 security 로그인페이지
                .loginProcessingUrl("/login")           //로그인 form에서 실행 POST
                .usernameParameter("email")             //로그인시 아이디
                .passwordParameter("password")          //로그인시 비밀번호
                .defaultSuccessUrl("/")                 //로그인 성공시 url
                .failureHandler(customFailHandler)      //실패시 핸들러
//                .failureUrl("/")                      //로그인 실패시 url
                .and()         //그리고
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))     //logout 입력시 security 로그아웃
                .logoutSuccessUrl("/");                                                //로그아웃 성공시 url
        return http.build();
    }
    @Bean  // 비밀번호 암호화
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
