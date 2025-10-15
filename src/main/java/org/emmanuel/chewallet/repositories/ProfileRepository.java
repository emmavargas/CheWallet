package org.emmanuel.chewallet.repositories;

import org.emmanuel.chewallet.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
