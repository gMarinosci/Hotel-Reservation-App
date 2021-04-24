package se.scandium.hotelproject.converter;

import se.scandium.hotelproject.dto.AuthorityDto;
import se.scandium.hotelproject.entity.Authority;

public interface AuthorityConverter {

    AuthorityDto convertAuthorityToDto(Authority authority);

    Authority convertDtoToAuthority(AuthorityDto authorityDto);
}
