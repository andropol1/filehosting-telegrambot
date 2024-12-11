package ru.andropol1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andropol1.entity.AppPhoto;

@Repository
public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {

}
