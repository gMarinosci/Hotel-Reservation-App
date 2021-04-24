package se.scandium.hotelproject.converter;

import org.springframework.stereotype.Component;
import se.scandium.hotelproject.dto.AuthorityDto;
import se.scandium.hotelproject.entity.Authority;

@Component
public class AuthorityConverterImpl implements AuthorityConverter {

    @Override
    public AuthorityDto convertAuthorityToDto(Authority authority) {

        AuthorityDto authorityDto = null;
        if (authority != null) {
            authorityDto = new AuthorityDto();
            authorityDto.setId(authority.getId());
            authorityDto.setName(authority.getName());
        }
        return authorityDto;
    }

    @Override
    public Authority convertDtoToAuthority(AuthorityDto authorityDto) {
        Authority authority = null;
        if (authorityDto != null) {
            authority = new Authority();
            authority.setId(authorityDto.getId());
            authority.setName(authorityDto.getName());
        }
        return authority;
    }

}
