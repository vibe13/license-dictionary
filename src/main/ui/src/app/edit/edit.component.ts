import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router, UrlSegment} from "@angular/router";
import {EmptyLicense, License, LicenseApprovalStatus, LicenseApprovalStatusList, EmptyLicenseApprovalStatus, LicenseService} from "../license.service";
import {AuthService} from "../auth.service";

@Component({
    selector: 'app-edit',
    templateUrl: './edit.component.html',
    styleUrls: ['./edit.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class EditComponent implements OnInit {

    license: License;
    selectedLicensesStatus: number;
    id: number;
    licensesStatusList: LicenseApprovalStatus[] = [];

    licenseCodeSafeCopy: string;
    licenseFedoraNameSafeCopy: string;
    licenseSpdxNameSafeCopy: string;

    errorMessage: string;

    constructor(private route: ActivatedRoute,
        private router: Router,
        private licenseService: LicenseService,
        private authService: AuthService) {

    }

    // loadToEdit()

    ngOnInit() {
        this.route.url.subscribe((segments: UrlSegment[]) => {

            this.licenseService.getLicensesApprovalStatus().subscribe(
                statusList => this.licensesStatusList = statusList.entries
            );

            if (segments[1]) {
                this.id = Number(segments[1]);
                console.log("a license id was passed for editing: ", this.id);
                this.licenseService.getLicense(this.id).subscribe(
                    license => this.initializeLicense(license)
                );
            }
            else {
                this.license = new EmptyLicense();
                this.licenseCodeSafeCopy = this.license.code;
                this.licenseFedoraNameSafeCopy = this.license.fedoraName;
                this.licenseSpdxNameSafeCopy = this.license.spdxName;
                this.selectedLicensesStatus = -1;
            }
        });

        this.authService.assureLoggedIn();
    }

    initializeLicense(license: License) {
        this.license = license;
        this.selectedLicensesStatus = this.license.licenseApprovalStatus.id;
        this.licenseCodeSafeCopy = license.code;
        this.licenseFedoraNameSafeCopy = license.fedoraName;
        this.licenseSpdxNameSafeCopy = license.spdxName;
    }

    saveLicense() {

        for (var j = 0; j < this.licensesStatusList.length; j++) {
            if (this.licensesStatusList[j].id == this.selectedLicensesStatus) {
                this.license.licenseApprovalStatus = this.licensesStatusList[j];
            }
        }

        if (this.id) {
            this.licenseService.updateLicense(this.id, this.license).subscribe(
                license =>
                    this.router.navigate(["/"]),
                error => {
                    console.log("error", error);
                    this.errorMessage = error
                }
            );
        } else {
            this.licenseService.addLicense(this.license).subscribe(
                license =>
                    this.router.navigate(["/"]),
                error => {
                    console.log("error", error);
                    this.errorMessage = error
                }
            );
        }
    }

    isStatusSelected() {
        return (this.selectedLicensesStatus != -1);
    }

    reset = function(form) {
        if (form) {
            form.$setPristine(true);
            form.$setUntouched(true);
        }
        this.license = new EmptyLicense();
        this.selectedLicensesStatus = -1;
    };

}

