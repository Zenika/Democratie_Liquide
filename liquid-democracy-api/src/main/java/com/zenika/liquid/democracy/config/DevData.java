package com.zenika.liquid.democracy.config;

import com.zenika.liquid.democracy.api.category.persistence.CategoryRepository;
import com.zenika.liquid.democracy.api.channel.persistence.ChannelRepository;
import com.zenika.liquid.democracy.api.subject.persistence.SubjectRepository;
import com.zenika.liquid.democracy.model.Category;
import com.zenika.liquid.democracy.model.Channel;
import com.zenika.liquid.democracy.model.Proposition;
import com.zenika.liquid.democracy.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Component
public class DevData {

    private final SubjectRepository subjectRepository;

    private final CategoryRepository categoryRepository;

    private final ChannelRepository channelRepository;

    @Autowired
    public DevData(SubjectRepository subjectRepository, CategoryRepository categoryRepository, ChannelRepository channelRepository) {
        this.subjectRepository = subjectRepository;
        this.categoryRepository = categoryRepository;
        this.channelRepository = channelRepository;
    }

    @PostConstruct
    public void init() {

        categoryRepository.deleteAll();
        channelRepository.deleteAll();
        subjectRepository.deleteAll();

        final Category cat1 = new Category();
        cat1.setTitle("Outils et compagnie");
        categoryRepository.insert(cat1);

        final Category cat2 = new Category();
        cat2.setTitle("Du LOL");
        categoryRepository.insert(cat2);

        final Channel chan1 = new Channel();
        chan1.setTitle("rennes");
        chan1.setDescription("Les bretons !");
        channelRepository.insert(chan1);

        final Channel chan2 = new Channel();
        chan2.setTitle("paris");
        chan2.setDescription("Coucou la capitale");
        channelRepository.insert(chan2);

        final Channel chan3 = new Channel();
        chan3.setTitle("lyon");
        channelRepository.insert(chan3);

        final Channel chan4 = new Channel();
        chan4.setTitle("lille");
        channelRepository.insert(chan4);

        final Subject subject = new Subject();
        subject.setTitle("DeadLine Test Title");
        subject.setDescription("DeadLine Test Description");
        subject.setCollaboratorId("corentin.cocoual@gmail.com");
        subject.setSubmitDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        subject.setDeadLine(Date.from(Instant.now().minus(2, ChronoUnit.HOURS)));
        subject.setMaxPoints(10);

        final List<Proposition> propositions = new ArrayList<Proposition>();
        final Proposition proposition1 = new Proposition();
        proposition1.setTitle("Proposition 1 Title");
        proposition1.setDescription("Proposition 1 Description");
        propositions.add(proposition1);

        final Proposition proposition2 = new Proposition();
        proposition2.setTitle("Proposition 2 Title");
        proposition2.setDescription("Proposition 2 Description");
        propositions.add(proposition2);

        subject.setPropositions(propositions);

        subjectRepository.insert(subject);

        for (int i = 0; i < 10; i++) {
            Subject subj = new Subject();
            subj.setTitle("DeadLine Test Title n°" + i);
            subj.setDescription("DeadLine Test Description n°" + i);
            subj.setCollaboratorId("corentin.cocoual@gmail.com");
            subj.setSubmitDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
            subj.setDeadLine(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));
            subj.setMaxPoints(10);

            List<Proposition> props = new ArrayList<Proposition>();
            Proposition prop1 = new Proposition();
            prop1.setTitle("Proposition 1 Title");
            prop1.setDescription("Proposition 1 Description");
            propositions.add(prop1);

            Proposition prop2 = new Proposition();
            prop2.setTitle("Proposition 2 Title");
            prop2.setDescription("Proposition 2 Description");
            propositions.add(prop2);

            subj.setPropositions(propositions);

            subjectRepository.insert(subj);
        }
    }

}
