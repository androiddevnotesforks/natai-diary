<?php

namespace App\Diary\Entity;

use App\Auth\Entity\User;
use App\Diary\Repository\NoteRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Uid\Uuid;

#[ORM\Entity(repositoryClass: NoteRepository::class)]
class Note
{
    #[ORM\Id]
    #[ORM\Column(type: 'uuid', unique: true)]
    private Uuid $id;

    #[ORM\Column(length: 255, nullable: true)]
    private ?string $title = null;

    #[ORM\Column(type: Types::TEXT, nullable: true)]
    private ?string $content = null;

    #[ORM\Column(type: Types::DATE_IMMUTABLE)]
    private \DateTimeImmutable $actualDate;

    #[ORM\Column]
    private \DateTimeImmutable $createdAt;

    #[ORM\Column]
    private \DateTimeImmutable $updatedAt;

    #[ORM\Column(nullable: true)]
    private ?\DateTimeImmutable $deletedAt = null;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(nullable: false)]
    private User $user;

    /** @var NoteTag[] */
    #[ORM\OneToMany(mappedBy: 'note', targetEntity: NoteTag::class)]
    private iterable $tags;

    public function __construct(
        Uuid $id,
        User $user,
        \DateTimeImmutable $actualDate,
        ?string $title,
        ?string $content,
        iterable $tags,
    )
    {
        $this->id = $id;
        $this->user = $user;
        $this->actualDate = $actualDate;
        $this->title = $title;
        $this->content = $content;
        $this->createdAt = new \DateTimeImmutable();
        $this->updatedAt = new \DateTimeImmutable();
        $this->tags = $tags;
    }
}