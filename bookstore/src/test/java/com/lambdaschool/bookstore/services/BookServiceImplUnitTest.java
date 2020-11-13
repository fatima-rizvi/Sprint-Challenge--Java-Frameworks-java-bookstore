package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookServiceImplUnitTest {

    @Autowired
    BookService bookService;

    @Before
    public void setUp() throws Exception {
        //mock -> fake data
        //stubs -> fake method
        //Java mock = stub -> mocks

        MockitoAnnotations.initMocks(this);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() { //Works
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void findBookById() { //Works!

        assertEquals("Test The Da Vinci Code", //Who painted the Mona Lisa? Mona Lisa. Da Vinky?
                bookService.findBookById(28)
                    .getTitle());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findBookByIdNotFound() {

        assertEquals("Test The Da Vinci Code",
                bookService.findBookById(100000)
                    .getTitle());
    }

    @Test
    public void z_delete() {

        bookService.delete(28);
        assertEquals(4, bookService.findAll().size());

    }

    @Test
    public void z_save() {

        Section s9 = new Section("Fiction");
        s9.setSectionid(1);
        Author a9 = new Author("Geronimo", "Stilton");
        a9.setAuthorid(1);

        Book testBook = new Book("Test Red Pizzas", "1239287288289", 2008, s9);
        testBook.getWrotes().add(new Wrote(a9, new Book()));
        assertEquals(testBook.getTitle(), "Test Red Pizzas");

    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
    }
}