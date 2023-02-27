<?php

namespace App\Attachment\Repository;

use App\Attachment\Entity\PendingAttachment;
use App\Auth\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<PendingAttachment>
 *
 * @method PendingAttachment|null find($id, $lockMode = null, $lockVersion = null)
 * @method PendingAttachment|null findOneBy(array $criteria, array $orderBy = null)
 * @method PendingAttachment[]    findAll()
 * @method PendingAttachment[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class PendingAttachmentRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, PendingAttachment::class);
    }

    public function save(PendingAttachment $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(PendingAttachment $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    /***
     * @param string[] $attachments
     * @return PendingAttachment[]
     */
    public function findAllByIds(User $user, array $attachments): array
    {
        return $this->createQueryBuilder('pa')
            ->andWhere('pa.id IN (:ids)')
            ->setParameter('ids', $attachments)
            ->andWhere('pa.user = :user')
            ->setParameter('user', $user->getId())
            ->getQuery()
            ->getResult();
    }

    /***
     * @return array<PendingAttachment>|PendingAttachment[]
     */
    public function findExpiredAttachments(): array
    {
        // find all attachments that are expired for at least 5 minutes
        return $this->createQueryBuilder('pa')
            ->andWhere('pa.expiresAt < :expired')
            ->setParameter('expired', new \DateTime('-5 minutes'))
            ->getQuery()
            ->getResult();
    }
}