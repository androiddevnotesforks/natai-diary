<?php

namespace App\Tests\Functional\Diary\Controller;

use App\Auth\DataFixtures\UserFixture;
use App\Auth\Entity\User;
use App\Diary\Controller\FindAllNotesAction;
use App\Tests\AbstractFunctionalTest;
use Symfony\Component\HttpFoundation\Response;

/**
 * @see FindAllNotesAction
 */
class FindAllNotesActionTest extends AbstractFunctionalTest
{
    public function testGetAccessAllNotesNotLoggedIn(): void
    {
        $client = static::createClient();
        $response = $client->request('GET', '/api/v1/notes');

        $this->assertEquals(Response::HTTP_UNAUTHORIZED, $response->getStatusCode());
    }

    public function testGetAccessAllNotes(): void
    {
        $userRepository = self::getContainer()->get('doctrine')->getRepository(User::class);
        $user = $userRepository->find(UserFixture::USER_ID);

        $client = static::createClient();
        $client->loginUser($user);

        $response = $client->request('GET', '/api/v1/notes');

        dump($response->getContent());

        $this->assertEquals(Response::HTTP_OK, $response->getStatusCode());
    }
}