package org.jboss.license.dictionary;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Project")
@Table(name = "project", indexes = { @Index(name = "idx_project", columnList = "ecosystem,key") })
@ToString
@EqualsAndHashCode
public class ProjectDBEntity {

    public static final String SEQUENCE_NAME = "project_id_seq";

    @Id
    @GeneratedValue(generator = SEQUENCE_NAME)
    @GenericGenerator(name = SEQUENCE_NAME, strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = SEQUENCE_NAME), @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "100") })
    @Getter
    private Integer id;

    @NotNull
    @Size(max = 10)
    @Column(name = "ecosystem", length = 10)
    @Getter
    @Setter
    private String ecosystem;

    @NotNull
    @Column(name = "key")
    @Getter
    @Setter
    private String key;

    @OneToMany(mappedBy = "project")
    private Set<ProjectVersionDBEntity> projectVersions;

    public ProjectDBEntity() {
        this.projectVersions = new HashSet<ProjectVersionDBEntity>();
    }

    public void addProjectVersion(ProjectVersionDBEntity projectVersion) {
        this.projectVersions.add(projectVersion);
    }

}
