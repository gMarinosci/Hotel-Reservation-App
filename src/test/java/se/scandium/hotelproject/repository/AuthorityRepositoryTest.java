package se.scandium.hotelproject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.scandium.hotelproject.entity.Authority;

import java.util.List;

@DataJpaTest
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository testObject;

    @BeforeEach
    private void setup() {
        Authority authorityRead = new Authority("READ");
        Authority authorityWrite = new Authority("WRITE");
        testObject.save(authorityRead);
        testObject.save(authorityWrite);
    }

    @Test
    @DisplayName("test_find_all_size_2")
    public void test_find_all_size_2() {
        List<Authority> authorityList = (List<Authority>) testObject.findAll();
        int actualSize = authorityList.size();
        int expectedSize = 2;
        assertEquals(expectedSize, actualSize);
    }

}
