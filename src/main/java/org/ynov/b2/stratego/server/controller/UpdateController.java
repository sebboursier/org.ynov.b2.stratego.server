/**
 *
 */
package org.ynov.b2.stratego.server.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ynov.b2.stratego.server.jpa.model.Etudiant;
import org.ynov.b2.stratego.server.jpa.model.Team;
import org.ynov.b2.stratego.server.jpa.model.TeamGroupe;
import org.ynov.b2.stratego.server.jpa.repository.EtudiantRepository;
import org.ynov.b2.stratego.server.jpa.repository.GameRepository;
import org.ynov.b2.stratego.server.jpa.repository.MoveRepository;
import org.ynov.b2.stratego.server.jpa.repository.PlayerRepository;
import org.ynov.b2.stratego.server.jpa.repository.TeamRepository;

/**
 * @author sebboursier
 *
 */
@RestController
@RequestMapping("update")
public class UpdateController {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private MoveRepository moveRepository;

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	public JavaMailSender emailSender;

	@Value("${app.debug}")
	private boolean appDebug;

	@RequestMapping("generateUUID")
	public HttpEntity<Map<Integer, UUID>> generateUUID(@RequestParam("ids") int[] ids) {
		final Map<Integer, UUID> uuids = new HashMap<>();
		for (int id : ids) {
			if (id != 0) {
				Team team = teamRepository.getOne(id);
				final UUID uuid = UUID.randomUUID();
				team.setUuid(uuid.toString());
				team = teamRepository.save(team);
				uuids.put(id, uuid);
				sendMailUuid(team);
			}
		}
		return new ResponseEntity<>(uuids, HttpStatus.OK);
	}

	@RequestMapping("resetHard")
	public HttpEntity<?> resetHard() {

		moveRepository.deleteAll();
		gameRepository.deleteAll();
		playerRepository.deleteAll();
		etudiantRepository.deleteAll();
		teamRepository.deleteAll();

		teamRepository.save(new Team(-1, TeamGroupe.TEST, "TEST_1"));
		teamRepository.save(new Team(-2, TeamGroupe.TEST, "TEST_2"));
		final Team seb = new Team(-3, TeamGroupe.PEDA,
				new Etudiant("BOURSIER", "Sébastien", "sebastien.boursier@ynov.com"));
		seb.setName("Boubou");
		seb.setUuid("c6f2173e-b117-4707-a063-bc71f68ea2e1");
		teamRepository.save(seb);

		if (appDebug) {
			return generateUUID(IntStream.rangeClosed(-2, -1).toArray());
		} else {
			teamRepository
					.save(new Team(1, TeamGroupe.A, new Etudiant("CHARLES", "Amandine", "amandine.charles@ynov.com"),
							new Etudiant("MEHAYE", "Clément", "clement.mehaye@ynov.com")));
			teamRepository.save(new Team(2, TeamGroupe.A, new Etudiant("BURLE", "Clelia", "clelia.burle@ynov.com"),
					new Etudiant("PENA", "Maël", "mael.pena@ynov.com")));
			teamRepository.save(
					new Team(3, TeamGroupe.A, new Etudiant("WILFART", "Louis-Armand", "louisarmand.wilfart@ynov.com"),
							new Etudiant("GERMAIN", "Maxim", "maxim.germain@ynov.com")));
			teamRepository.save(new Team(4, TeamGroupe.A, new Etudiant("SALIF", "Ervin", "ervin.salif@ynov.com"),
					new Etudiant("BOURG", "Valentin", "valentin.bourg@ynov.com")));
			teamRepository
					.save(new Team(5, TeamGroupe.A, new Etudiant("DOMINGUE", "Anthony", "anthony.domingue@ynov.com"),
							new Etudiant("SELLAN", "Etienne", "etienne.sellan@ynov.com")));
			teamRepository
					.save(new Team(6, TeamGroupe.A, new Etudiant("NICOLLEAU", "Antoine", "antoine.nicolleau@ynov.com"),
							new Etudiant("BOISSON", "Verner", "verner.boisson@ynov.com")));
			teamRepository
					.save(new Team(7, TeamGroupe.A, new Etudiant("GARNIER", "Alexandre", "alexandre.garnier@ynov.com"),
							new Etudiant("ROUGAGNOU", "Boris", "boris.rougagnou@ynov.com")));
			teamRepository
					.save(new Team(8, TeamGroupe.A, new Etudiant("LAFUENTE", "Florian", "florian.lafuente@ynov.com"),
							new Etudiant("ERISSET", "Lucas", "lucas.erisset@ynov.com")));
			teamRepository.save(new Team(9, TeamGroupe.A, new Etudiant("GALMOT", "Benoit", "benoit.galmot@ynov.com"),
					new Etudiant("SEGHIR", "Souleimane", "souleimane.seghir@ynov.com")));
			teamRepository.save(new Team(10, TeamGroupe.A, new Etudiant("DINH", "Jonathan", "jonathan.dinh@ynov.com"),
					new Etudiant("HERNANDEZ", "Theo", "theo.charron@ynov.com")));
			teamRepository.save(new Team(11, TeamGroupe.A, new Etudiant("ORLANDO", "Damien", "damien.orlando@ynov.com"),
					new Etudiant("LE COQ", "Guillaume", "guillaume.lecoq@ynov.com")));
			teamRepository
					.save(new Team(12, TeamGroupe.B, new Etudiant("BORDAS", "Matthieu", "matthieu.bordas@ynov.com"),
							new Etudiant("RAFFY", "Valentin", "valentin.raffy@ynov.com")));
			teamRepository.save(new Team(13, TeamGroupe.B, new Etudiant("LYS", "Florian", "florian.lys@ynov.com"),
					new Etudiant("ANTON", "Guillaume", "guillaume.anton@ynov.com")));
			teamRepository.save(new Team(14, TeamGroupe.B,
					new Etudiant("SCOTTO D'ANIELLO", "Anthony", "anthony.scottodaniello@ynov.com"),
					new Etudiant("BORIE", "Florian", "florian.borie@ynov.com"),
					new Etudiant("HATUEL", "Thomas", "thomas.hatuel@ynov.com")));
			teamRepository.save(new Team(15, TeamGroupe.B, new Etudiant("DAUGUEN", "Arnaud", "arnaud.dauguen@ynov.com"),
					new Etudiant("OLLIVIER DROLSHAGEN", "Felix", "felix.ollivierdrolshagen@ynov.com")));
			teamRepository.save(new Team(16, TeamGroupe.B, new Etudiant("MUSSEAU", "Maxime", "maxime.musseau@ynov.com"),
					new Etudiant("LAZAR", "Mohamed", "mohamed.lazar@ynov.com")));
			teamRepository.save(new Team(17, TeamGroupe.B, new Etudiant("PARIS", "Axel", "axel.paris@ynov.com"),
					new Etudiant("HAY", "Théo", "theo.hernandez@ynov.com")));
			teamRepository
					.save(new Team(18, TeamGroupe.B, new Etudiant("PICARD", "Benjamin", "benjamin.picard@ynov.com"),
							new Etudiant("BLANCHET", "Noémie", "noemie.blanchet@ynov.com")));
			teamRepository
					.save(new Team(19, TeamGroupe.B, new Etudiant("NICOLAS", "Florent", "florent.nicolas@ynov.com"),
							new Etudiant("GANS", "Quentin", "quentin.gans@ynov.com")));
			teamRepository.save(new Team(20, TeamGroupe.B, new Etudiant("SELLA", "Justin", "justin.sella@ynov.com"),
					new Etudiant("FERREIRA", "Théo", "theo.hay@ynov.com")));
			teamRepository
					.save(new Team(21, TeamGroupe.B, new Etudiant("PALLARD", "Corentin", "corentin.pallard@ynov.com"),
							new Etudiant("CONSEJO", "Lucas", "lucas.consejo@ynov.com")));
			teamRepository.save(new Team(22, TeamGroupe.A, new Etudiant("PARASOTE", "Bryan", "bryan.parasote@ynov.com"),
					new Etudiant("BEN EL FAHSI", "Sami", "sami.benelfahsi@ynov.com")));
			teamRepository
					.save(new Team(23, TeamGroupe.B, new Etudiant("CLÉMENÇON", "Gabriel", "gabriel.clemencon@ynov.com"),
							new Etudiant("BACLE", "Jullian", "jullian.bacle@ynov.com")));
			teamRepository.save(new Team(24, TeamGroupe.B, new Etudiant("HIVERT", "Paul", "paul.hivert@ynov.com"),
					new Etudiant("CHARLES", "Reynald", "reynald.charles@ynov.com")));
			teamRepository.save(new Team(25, TeamGroupe.A, new Etudiant("POMMIER", "Damien", "damien.pommier@ynov.com"),
					new Etudiant("LESPRIT", "Jeremy", "jeremy.lesprit@ynov.com")));
			teamRepository.save(new Team(26, TeamGroupe.A, new Etudiant("SOMBRUN", "Joé", "joe.sombrun@ynov.com"),
					new Etudiant("PHÉBIDIAS", "Leïla", "leila.phebidias@ynov.com")));
			teamRepository.save(new Team(27, TeamGroupe.B, new Etudiant("CATALLO", "Loris", "loris.catallo@ynov.com"),
					new Etudiant("PLATEL", "Macarthy-Lee", "macarthylee.platel@ynov.com")));
			teamRepository.save(new Team(28, TeamGroupe.A,
					new Etudiant("STEFANAGGI", "Antoine-Dominique", "antoinedominique.stefanaggi@ynov.com"),
					new Etudiant("CHARRON", "Théo", "theo.ferreira@ynov.com")));
		}

		return generateUUID(IntStream.rangeClosed(-2, 28).toArray());
	}

	private void sendMailUuid(final Team team) {
		if (team.getEtudiants() != null && !team.getEtudiants().isEmpty()) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("sebastien.boursier@ynov.com");
			message.setTo(team.getEtudiants().stream().map(etudiant -> etudiant.getEmail()).toArray(String[]::new));
			message.setSubject("[STRATEGO] Team UUID");
			message.setText(
					"Bien le bonjour,\nvoici l'UUID de votre team pour le projet Stratego :\n<uuid>\nConservez le précieusement et secretement, car c'est ce qui vous identifie lors d'une partie.\n[Mail envoyé automatiquement par le serveur]"
							.replaceFirst("<uuid>", team.getUuid().toString()));
			emailSender.send(message);
		}
	}
}
