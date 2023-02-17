import {ReactNode} from "react";
import {Disclosure, Transition} from "@headlessui/react";
import Image from "next/image";
import flowerSvg from '../../../../public/assets/img/flower.svg';
import Link from "next/link";
import {NextRouter, useRouter} from "next/router";
import {authService} from "../../auth/services/authService";
import {useAppStateManager} from "../state";
import {classNames} from "../../../utils/classNames";
import UserDropdownMenu from "../../auth/components/userDropdownMenu";
import {UserDto} from "../../../api/apiSchemas";
import {CheckCircleIcon} from "@heroicons/react/20/solid";
import {useTranslations} from "use-intl";
import {Animate} from "./Animate";


const Header = ({router}: { router: NextRouter }) => {
    const {isLoading} = useAppStateManager()

    return (
        <Disclosure as="nav">
            {({open}) => (
                <>
                    <header className="p-4 bg-white text-gray-100">
                        <div className="xl:container mx-auto flex justify-between h-10">
                            <Link href="/" aria-label="Back to homepage"
                                  className="flex items-center p-2">

                                <ProjectLogo/>

                                <span className="ml-2 color-name font-poppins font-bold text-xl">Natai</span>
                            </Link>
                            <DesktopNavBar router={router}/>
                            <Disclosure.Button className="lg:hidden">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                     stroke="currentColor"
                                     className="w-6 h-6 text-gray-100">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                          d="M4 6h16M4 12h16M4 18h16"></path>
                                </svg>
                            </Disclosure.Button>
                        </div>
                    </header>

                    <Animate>
                        <Disclosure.Panel>
                            <MobileNavBar router={router}/>
                        </Disclosure.Panel>
                    </Animate>
                </>
            )}
        </Disclosure>
    )
}


const DesktopNavBar = ({router}: { router: NextRouter }) => {
    const from = authService.createUrlForRedirect(router)

    const {user} = useAppStateManager()

    const isActive = (path: string) => {
        return router.pathname === path
    }

    return (
        <>
            <ul className="items-stretch hidden space-x-3 lg:flex -my-4">
                <li className="flex">
                    <Link href={"/"}
                          className={classNames("flex items-center px-4 -mb-1 border-b-2 border-transparent nav-item", isActive("/") ? "nav-item-active" : "")}>
                        Home
                    </Link>
                </li>
                <li className="flex">
                    <Link href={"/diary"}
                          className={classNames("flex items-center px-4 -mb-1 border-b-2 border-transparent nav-item", isActive("/diary") ? "nav-item-active" : "")}>
                        My Diary
                    </Link>
                </li>
                <li className="flex">
                    <Link href={"/stories"}
                          className={classNames("flex items-center px-4 -mb-1 border-b-2 border-transparent nav-item", isActive("/stories") ? "nav-item-active" : "")}>
                        Stories
                    </Link>
                </li>
                <li className="flex">
                    <Link href={"/static/contacts"}
                          className={classNames("flex items-center px-4 -mb-1 border-b-2 border-transparent nav-item", isActive("/static/contacts") ? "nav-item-active" : "")}>
                        Contacts
                    </Link>
                </li>
            </ul>
            <div className="items-center flex-shrink-0 hidden lg:flex">
                {user ? (
                    <UserDropdownMenu user={user}/>
                ) : (
                    <>
                        <Link href={"/login" + from} className="self-center px-8 py-3 font-semibold rounded color-brand">Sign in</Link>
                        <Link href={"/registration" + from}
                              className="self-center px-8 rounded-3xl py-3 font-semibold rounded bg-brand hover:bg-opacity-20">Sign up</Link>
                    </>
                )}

            </div>
        </>
    )
}

function MobileUserDropdownMenu({user}: { user: UserDto }) {
    const t = useTranslations("MobileUserDropdownMenu")
    return (
        <div className="pt-4 pb-3 border-t border-gray-700">
            <div className="flex items-center px-5">
                <div className="flex-shrink-0">
                    <div className="flex justify-center align-center h-10 w-10 rounded-full bg-red-600">
                        <span className="font-bold text-white uppercase m-auto">
                            {user.name.at(0)}
                        </span>
                    </div>
                </div>
                <div className="ml-3">
                    <div className="text-base font-medium leading-none text-white">{user.name}</div>
                    <div className="text-sm font-medium leading-none text-gray-400">
                        {user.email}
                        {user.isEmailVerified && (<CheckCircleIcon className={"ml-1 inline text-green-600 h-4 w-4"}/>)}
                    </div>
                </div>
            </div>
            <div className="mt-3 px-2 space-y-1">
                <Link className="block px-3 py-2 rounded-md text-base font-medium text-gray-400 hover:text-white hover:bg-gray-700" href="/settings">
                    {t("Settings")}
                </Link>
            </div>
        </div>
    )
}

const MobileNavBar = ({router}: { router: NextRouter }) => {
    const {user} = useAppStateManager()
    const from = authService.createUrlForRedirect(router);

    return (
        <>
            <ul className="bg-gray-600 px-2 pt-2 pb-3 space-y-1 sm:px-3">
                <li>
                    <Link href={"/"}
                          className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Home</Link>
                </li>
                <li>
                    <Link href={"/diary"}
                          className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">My
                        Diary</Link>
                </li>
                <li>
                    <Link href={"/stories"}
                          className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Stories</Link>
                </li>
                <li>
                    <Link href={"/static/contacts"}
                          className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Contacts</Link>
                </li>
                {user ? (
                    <>
                        <MobileUserDropdownMenu user={user}/>
                    </>
                ) : (
                    <>
                        <li>
                            <Link href={"/login" + from}
                                  className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Login</Link>
                        </li>
                        <li>
                            <Link href={"/registration" + from}
                                  className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium">Registration</Link>
                        </li>
                    </>
                )}


            </ul>
        </>
    )
}

const Footer = () => {
    return (
        <footer className="px-4 py-8 bg-gray-800 text-gray-400">
            <div
                className="container flex flex-wrap items-center justify-center mx-auto space-y-4 sm:justify-between sm:space-y-0">
                <div className="flex flex-row pr-3 space-x-4 sm:space-x-8">
                    <ProjectLogo/>
                    <ul className="flex flex-wrap items-center space-x-4 sm:space-x-8">
                        <li>
                            <Link href={"/static/terms"}>Terms of Use</Link>
                        </li>
                        <li>
                            <Link href={"/static/privacy"}>Privacy</Link>
                        </li>
                    </ul>
                </div>
                <ul className="flex flex-wrap pl-3 space-x-4 sm:space-x-8">
                    <li>
                        <Link href="#">Instagram</Link>
                    </li>
                    <li>
                        <Link href="#">Facebook</Link>
                    </li>
                    <li>
                        <Link href="#">Twitter</Link>
                    </li>
                </ul>
            </div>
        </footer>
    )
}

const MainLayout = ({children, containerClass}: { children: ReactNode, containerClass?: string }) => {
    const router = useRouter()
    return (
        <div className="min-h-screen flex flex-col">
            <Header router={router}/>
            <div className={classNames(containerClass || "flex-1 container mx-auto p-3 sm:p-0")}>
                {children}
            </div>
            <Footer/>
        </div>
    );
}

export const ProjectLogo = () => {
    return (
        <div
            className="flex items-center justify-center flex-shrink-0 w-12 h-12 rounded-full bg-flower">
            <Image src={flowerSvg} alt={"Flower Natai Diary Logo"} className={"w-7 h-7"}/>
        </div>
    )
}

export default MainLayout;